package com.chatConnect.backend.Service;


import com.chatConnect.backend.Event.GroupMessageCreatedEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;
import com.chatConnect.backend.Repo.GroupMessageRepo;
import com.chatConnect.backend.Repo.GroupRepo;
import com.chatConnect.backend.Repo.UserRepo;
import com.chatConnect.backend.Config.PresenceEventListener;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class GroupService {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    UserRepo userRepo;

    @Autowired
    GroupRepo groupRepo;

    @Autowired
    ChatMessageRepo chatRepo;

    @Autowired
    private LanguageDetectionService languageDetectionService;

    @Autowired
    GroupMessageRepo groupMessageRepo;

    @Autowired
    PresenceEventListener presenceEventListener;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired

    EntityManager entityManager;
    public ResponseEntity<GroupResponseDTO> createGroup(String username, GroupDTO groupDTO) {
        System.out.println("groupdto"+groupDTO.toString());

        Users admin=userRepo.findByUsername(username);
        List<Users> groupMembers=new ArrayList<>();
        //add admin to group
        groupMembers.add(admin);
        for(String memberName:groupDTO.getGroupMembers()){
            groupMembers.add(userRepo.findByUsername(memberName));
        }
        GroupChat group=new GroupChat(groupDTO.getGroupName(),groupMembers,admin);
        GroupChat savedGroup=groupRepo.save(group);
        List<String> savedGroupMembers=new ArrayList<>();
        for(Users user:savedGroup.getGroupMembers()){
            savedGroupMembers.add(user.getUsername());
        }


        GroupResponseDTO groupResponseDTO=new GroupResponseDTO(savedGroup.getId(), savedGroup.getGroupName(), savedGroup.getAdmin().getUsername(),savedGroupMembers,savedGroup.getCreatedAt());
        System.out.println("responsedto"+groupResponseDTO.toString());
        for(Users m: savedGroup.getGroupMembers()){
            simpMessagingTemplate.convertAndSendToUser(m.getUsername(),"/queue/groupInfo",groupResponseDTO);

        }
        return ResponseEntity.ok(groupResponseDTO);
    }

    @Transactional

    public List<GroupChat> getAllGroupsForUser(Users user){
        List<GroupChat> groups=groupRepo.findByGroupMembersContaining(user);
        for(GroupChat group:groups){
            System.out.println(group.getGroupMembers().size());
        }
        return groups;

    }

    public GroupChat getGroupById(long groupId) {
        Optional<GroupChat> groupChat= groupRepo.findById(groupId);
        if(groupChat.isPresent()){
            return groupChat.get();
        }
        return null;
    }

    public Map<String, Integer> getUnReadMessagesPerGroup(String username) {

        Map<String,Integer> mpp=new HashMap<>();
        Users receiver=userRepo.findByUsername(username);
        List<GroupChat> joinedGroups=getAllGroupsForUser(receiver);
        //List<ChatMessage> unReadMessages=new ArrayList<>();
        for(GroupChat group:joinedGroups){
            List<GroupMessage> messages=groupMessageRepo.findByGroupChatAndReadUsersNotContaining(group,receiver);
            mpp.put(group.getGroupName(),messages.size());
            //unReadMessages.addAll(messages);
        }
        return mpp;
    }

    public List<GroupMessageResponseDTO> getMessagesForGroup(String groupname) {

        GroupChat group=groupRepo.findByGroupName(groupname);
        System.out.println("groupname="+groupname);
        List<GroupMessage> messages=groupMessageRepo.findByGroupChat(group);
        List<GroupMessageResponseDTO> groupMessages=new ArrayList<>();
        for(GroupMessage groupMessage:messages){
            GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(groupMessage.getId(), groupMessage.getContent(),groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
            if(groupMessage.getDeliveredUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
                responseMsg.setStatus("DELIVERED");
            }
            if(groupMessage.getReadUsers().size()==groupMessage.getGroupChat().getGroupMembers().size()){
                responseMsg.setStatus("READ");
            }
            groupMessages.add(responseMsg);
        }
        return groupMessages;
    }

    public void markMessagesRead(String username, String groupname) {

        Users user=userRepo.findByUsername(username);
        GroupChat group=groupRepo.findByGroupName(groupname);
        List<GroupMessage> messages=groupMessageRepo.findByGroupChatAndReadUsersNotContaining(group,user);
        for(GroupMessage message:messages){
            message.addReadUsers(user);
            if(message.getReadUsers().size()==message.getGroupChat().getGroupMembers().size()){
                GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(message.getId(),message.getContent(),message.getCreatedAt(),"READ",message.getSender().getUsername(),message.getGroupChat().getGroupName());
                simpMessagingTemplate.convertAndSendToUser(message.getSender().getUsername(),"/queue/group/"+message.getGroupChat().getId(),responseMsg);
            }
            groupMessageRepo.save(message);
        }

    }

    @Transactional

    public  synchronized void markGroupMessageRead(String username, long msgId) {

        Users user=userRepo.findByUsername(username);
        Optional<GroupMessage> message= Optional.empty();
        int retries=10;
        for (int i = 0; i < retries; i++) {
            message = groupMessageRepo.findById(msgId);
            if (message.isPresent()) break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        if(message.isPresent()){

            GroupMessage m=message.get();

            groupMessageRepo.addReadUser(m.getId(),user.getId());
            //groupMessageRepo.save(m);
            /*try {
                Thread.sleep(9000); /
            } catch (InterruptedException ignored) {}
            for(Users users:m.getReadUsers()){
                System.out.println("read user name"+users.getUsername());
            }*/

            //Optional<GroupMessage> msgs=groupMessageRepo.findById(m.getId());

            entityManager.flush();
            entityManager.refresh(m);

            GroupMessage updatedMessage=groupMessageRepo.findById(m.getId()).get();
            System.out.println(username+" "+msgId+" "+updatedMessage.getReadUsers().size()+" "+updatedMessage.getGroupChat().getGroupMembers().size());
            /*for(Users readUser:updatedMessage.getReadUsers()){
                System.out.println("read username:"+readUser.getUsername());
            }*/
            if(updatedMessage.getReadUsers().size()==updatedMessage.getGroupChat().getGroupMembers().size()-1){

               for(Users users:m.getReadUsers()){
                   System.out.println(users.getUsername());
               }
                System.out.println("groupembers size="+m.getReadUsers().size());
                GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(updatedMessage.getId(),updatedMessage.getContent(),updatedMessage.getCreatedAt(),"READ",updatedMessage.getSender().getUsername(),updatedMessage.getGroupChat().getGroupName());
                simpMessagingTemplate.convertAndSendToUser(m.getSender().getUsername(),"/queue/group/"+updatedMessage.getGroupChat().getId(),responseMsg);
            }
            //groupMessageRepo.save(m);
        }
        else{

            System.out.println(username+"message is missing");
        }

    }

    @Transactional

    public void sendGroupMessage(String senderUsername, GroupMessageRequestDTO groupMessage) {
        Set<String> onlineUsers=presenceEventListener.getOnlineUsers();

        Users sender=userRepo.findByUsername(senderUsername);
        GroupChat group=groupRepo.findByGroupName(groupMessage.getGroupName());
        String lang=languageDetectionService.detectLanguage(groupMessage.getContent());
        GroupMessage createdMessage=new GroupMessage(sender,group,groupMessage.getContent(),lang);


        groupMessageRepo.save(createdMessage);

        List<Users> receivers=group.getGroupMembers();
        for(Users receiver:receivers){
            if(onlineUsers.contains(receiver.getUsername())){
                createdMessage.addDeliveredUsers(receiver);

            }
        }
        groupMessageRepo.save(createdMessage);

        //publish event to send message after commit
        eventPublisher.publishEvent(new GroupMessageCreatedEvent(createdMessage));









    }

    public List<GroupResponseDTO> search(String username, String keyword) {
        Users user=userRepo.findByUsername(username);
        List<GroupResponseDTO> groupChatDTOList=new ArrayList<>();
        List<GroupChat> groupChatList=groupRepo.findByGroupMembersContainingAndGroupNameContainingIgnoreCase(user,keyword);
        for(GroupChat groupChat:groupChatList){
            List<String> groupMembers=new ArrayList<>();
            for(Users groupMember:groupChat.getGroupMembers()){
                groupMembers.add(user.getUsername());
            }
            GroupResponseDTO groupResponseDTO=new GroupResponseDTO(groupChat.getId(), groupChat.getGroupName(),groupChat.getAdmin().getUsername(),groupMembers,groupChat.getCreatedAt());
            groupChatDTOList.add(groupResponseDTO);
        }
        return groupChatDTOList;
    }

    public ResponseEntity<Void> deleteMessage(String username, long msgId,String scope) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
