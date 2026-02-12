package com.chatConnect.backend.Config;

import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;
import com.chatConnect.backend.Repo.GroupMessageRepo;
import com.chatConnect.backend.Repo.UserRepo;
import com.chatConnect.backend.Service.GroupService;
import com.chatConnect.backend.Modal.*;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component

public class PresenceEventListener {

    private final Set<String> onlineUsers = ConcurrentHashMap.newKeySet();

    @Autowired

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatMessageRepo chatRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    @Lazy
    private GroupService groupService;

    @Autowired
    private GroupMessageRepo groupMessageRepo;

    public PresenceEventListener(SimpMessagingTemplate simpMessagingTemplate){
        this.simpMessagingTemplate=simpMessagingTemplate;
    }

    public Set<String> getOnlineUsers() {
        return onlineUsers;
    }

    @EventListener

    public void handleConnectedEvent(SessionConnectEvent event) throws InterruptedException {
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());
        UserPrincipal user= (UserPrincipal) accessor.getUser();
        System.out.println("Connected user : " + accessor.getUser());

        if(user!=null){
            onlineUsers.add(user.getUsername());
            broadCastOnline();


        }



    }

    @Transactional


    public void deliverGroupMessages(UserPrincipal user) {

        String username = user.getUsername();
        List<GroupMessage> undeliveredGroupMessages = new ArrayList<>();
        Users receiver=userRepo.findByUsername(username);
        List<GroupChat> joinedGroups=groupService.getAllGroupsForUser(userRepo.findByUsername(username));
        for(GroupChat groupChat:joinedGroups){
            System.out.println("groupchat"+groupChat);
            List<GroupMessage> messages=groupMessageRepo.findByGroupChatAndDeliveredUsersNotContaining(groupChat,receiver);



            for (GroupMessage E : messages) {
                Hibernate.initialize(E.getDeliveredUsers());
                System.out.println(E.getDeliveredUsers().size());

            }
            undeliveredGroupMessages.addAll(messages);
        }
        System.out.println(undeliveredGroupMessages);
        for(GroupMessage groupMessage:undeliveredGroupMessages){
            //groupMessage.getDeliveredUsers();
            groupMessage.addDeliveredUsers(receiver);
            groupMessageRepo.save(groupMessage);
            //System.out.println(chatMessage.getDeliveredUsers());
            //System.out.println("groupid"+chatMessage.getGroupChat().getId());
            GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(groupMessage.getId(), groupMessage.getContent(),groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
            //send msg to whoever subscribed to groupid
            if(groupMessage.getDeliveredUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
                responseMsg.setStatus("DELIVERED");
            }
            simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/group/"+groupMessage.getGroupChat().getId(),responseMsg);
            simpMessagingTemplate.convertAndSendToUser(groupMessage.getSender().getUsername(),"/queue/group/"+groupMessage.getGroupChat().getId(),responseMsg);
        }


    }


    public  void deliverPrivateMessages(UserPrincipal user) {

        List<ChatMessage> unDeliveredMessages=new ArrayList<>();
        String username=user.getUsername();
        Users receiver=userRepo.findByUsername(username);
        unDeliveredMessages=chatRepo.findByReceiverAndDeliveredFalse(receiver);
        for(ChatMessage chatMessage:unDeliveredMessages){
            chatMessage.setDelivered(true);
            chatRepo.save(chatMessage);
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"DELIVERED");
            simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/private",responseMsg);

            simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(), "/queue/private", responseMsg);

        }







    }

    @EventListener
    public void handleDisConnectedEvent(SessionDisconnectEvent event){
        StompHeaderAccessor accessor=StompHeaderAccessor.wrap(event.getMessage());
        UserPrincipal user= (UserPrincipal) accessor.getUser();
        if(user!=null){
            onlineUsers.remove(user.getUsername());

            broadCastOnline();
            broadCastLastSeen(user);

        }

    }

    @Transactional

    public void pushGroupInfoToUser(UserPrincipal userPrincipal) {
        Users user=userRepo.findByUsername(userPrincipal.getUsername());

        List<GroupChat> groups=groupService.getAllGroupsForUser(user);
        System.out.println(groups);
        List<GroupResponseDTO> groupResponseDTOS=new ArrayList<>();
        for(GroupChat group:groups){
            List<String> groupMembers=new ArrayList<>();
            for(Users members:group.getGroupMembers()){
                groupMembers.add(members.getUsername());
            }


            GroupResponseDTO groupResponseDTO=new GroupResponseDTO(group.getId(), group.getGroupName(), group.getAdmin().getUsername(),groupMembers,group.getCreatedAt());
            System.out.println(groupResponseDTO);
            groupResponseDTOS.add(groupResponseDTO);

        }
        simpMessagingTemplate.convertAndSendToUser(user.getUsername(),"/queue/groupInfo",groupResponseDTOS);
    }


    private void broadCastLastSeen(UserPrincipal userPrincipal){
        Users user=userRepo.findByUsername(userPrincipal.getUsername());
        user.setLastSeen(LocalDateTime.now());
        userRepo.save(user);
        UserDTO userDTO=new UserDTO(user.getId(),user.getUsername(),user.getLastSeen(),user.getProfileImage(),user.getPreferredLanguage());

        simpMessagingTemplate.convertAndSend("/topic/lastSeen",userDTO);


    }


    private void broadCastOnline() {
        System.out.println(onlineUsers);

            simpMessagingTemplate.convertAndSend("/topic/online",onlineUsers);
    }

    public void broadcastPrivate(UserPrincipal user){

        List<String> users=new ArrayList<>(onlineUsers); // to send as an array even if it is single user??sometimes if u send singleuser in array,it may transform into string(ain json),so in frontend,it expects array.make sure to send arry even if it is single element
        System.out.println("private:"+onlineUsers +" "+user.getUsername());
        simpMessagingTemplate.convertAndSendToUser(user.getUsername(),"/queue/online",users);
    }
}
