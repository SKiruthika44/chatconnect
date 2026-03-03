package com.chatConnect.backend.Service;


import com.chatConnect.backend.Event.GroupMessageCreatedEvent;
import com.chatConnect.backend.Event.GroupMessageDeletedEvent;
import com.chatConnect.backend.Event.GroupMessageEmojiCreatedEvent;
import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.*;
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
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    PresenceEventListener presenceEventListener;
    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @Autowired
    GroupMessageDeletedUserRepo groupMessageDeletedUserRepo;

    @Autowired
    GroupMessageEmojiReactionRepo groupMessageEmojiReactionRepo;

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

        for(GroupChat group:joinedGroups){
            int count=0;
            //List<GroupMessage> messages=groupMessageRepo.findByGroupChatAndReadUsersNotContaining(group,receiver);
            List<GroupMessage> allMessages=groupMessageRepo.findByGroupChat(group);
            for(GroupMessage groupMessage:allMessages){
                boolean exists=messageReadRepo.existsByMsgIdAndUserIdByCount(groupMessage.getMsg_id(), receiver.getId());
                if(!exists){
                    count+=1;
                }
            }
            mpp.put(group.getGroupName(),count);

        }
        return mpp;
    }

    public List<GroupMessageResponseDTO> getMessagesForGroup(String username,String groupname) {
        Users user=userRepo.findByUsername(username);

        GroupChat group=groupRepo.findByGroupName(groupname);
        System.out.println("groupname="+groupname);
        List<GroupMessage> messages=groupMessageRepo.findAllVisibleMessagesByUser(user,group,user.getId());
        List<GroupMessageResponseDTO> groupMessages=new ArrayList<>();
        for(GroupMessage groupMessage:messages){
            GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(groupMessage.getMsg_id(), groupMessage.getContent(),groupMessage.getCreatedAt(),"SENT",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());

            long deliveredCount=messageDeliveryRepo.countByMsgIdAndUserId(groupMessage.getMsg_id());
            long readCount=messageReadRepo.countReadUsers(groupMessage.getMsg_id());
            if(deliveredCount==groupMessage.getGroupChat().getGroupMembers().size()){
                responseMsg.setStatus("DELIVERED");
            }
            if(readCount==groupMessage.getGroupChat().getGroupMembers().size()){
                responseMsg.setStatus("READ");
            }

            if(groupMessage.isDeletedForEveryone()){
                responseMsg.setDeletedForEveryone(true);
                responseMsg.setContent(null);
            }
            Map<String,Long> emojisMap=new HashMap<>();
            List<Object[]> emojis=groupMessageRepo.findEmojisCountForMessage(groupMessage.getMsg_id());
            for(Object[] row:emojis){
                emojisMap.put((String)row[0],(Long)row[1]);
            }
            responseMsg.setEmojisCount(emojisMap);

            groupMessages.add(responseMsg);
        }
        return groupMessages;
    }

    public void markMessagesRead(String username, String groupname) {

        Users user=userRepo.findByUsername(username);
        GroupChat group=groupRepo.findByGroupName(groupname);
        //List<GroupMessage> messages=groupMessageRepo.findByGroupChatAndReadUsersNotContaining(group,user);
        List<GroupMessage> messages=groupMessageRepo.findByGroupChat(group);
        List<GroupMessage> unReadMessages=new ArrayList<>();
        for(GroupMessage groupMessage:messages){
            boolean exists=messageReadRepo.existsByMsgIdAndUserIdByCount(groupMessage.getMsg_id(),user.getId());
            if(!exists){
                unReadMessages.add(groupMessage);
            }
        }
        for(GroupMessage message:unReadMessages){

            MessageUserId messageUserId=new MessageUserId(message.getMsg_id(), user.getId());
            MessageRead messageRead=new MessageRead(messageUserId);
            messageReadRepo.save(messageRead);
            long readUsersCount=messageReadRepo.countReadUsers(message.getMsg_id());
            if(readUsersCount==message.getGroupChat().getGroupMembers().size()){
                GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(message.getMsg_id(),message.getContent(),message.getCreatedAt(),"READ",message.getSender().getUsername(),message.getGroupChat().getGroupName());
                simpMessagingTemplate.convertAndSendToUser(message.getSender().getUsername(),"/queue/group/"+message.getGroupChat().getId(),responseMsg);
            }

        }

    }

    @Transactional

    public  synchronized void markGroupMessageRead(String username, long msgId) {

        Users user=userRepo.findByUsername(username);
        Optional<GroupMessage> optionalMessage= groupMessageRepo.findByMsgId(msgId);


        if(optionalMessage.isPresent()) {
           GroupMessage groupMessage = optionalMessage.get();
           if(!groupMessage.getSender().equals(user)){ //sender cannot update read
               MessageUserId messageUserId=new MessageUserId(groupMessage.getMsg_id(), user.getId());
               MessageRead messageRead=new MessageRead(messageUserId);
               messageReadRepo.save(messageRead);
               GroupMessageResponseDTO responseMsg=new GroupMessageResponseDTO(groupMessage.getMsg_id(),groupMessage.getContent(),groupMessage.getCreatedAt(),"READ",groupMessage.getSender().getUsername(),groupMessage.getGroupChat().getGroupName());
               simpMessagingTemplate.convertAndSendToUser(groupMessage.getSender().getUsername(),"/queue/group/"+groupMessage.getGroupChat().getId(),responseMsg);


           }


        }

    }

    @Transactional

    public void sendGroupMessage(String senderUsername, GroupMessageRequestDTO groupMessage) {


        Users sender=userRepo.findByUsername(senderUsername);
        GroupChat group=groupRepo.findByGroupName(groupMessage.getGroupName());
        String lang=languageDetectionService.detectLanguage(groupMessage.getContent());
        GroupMessage createdMessage=new GroupMessage(sender,group,groupMessage.getContent(),lang);


        groupMessageRepo.save(createdMessage);

        //add sender to delivery and read
        MessageUserId senderMessageUserId=new MessageUserId(createdMessage.getMsg_id(), sender.getId());
        MessageDelivery senderMessageDelivery=new MessageDelivery(senderMessageUserId);
        messageDeliveryRepo.save(senderMessageDelivery);
        MessageRead messageRead=new MessageRead(senderMessageUserId);
        messageReadRepo.save(messageRead);

        Set<String> onlineUsers=presenceEventListener.getOnlineUsers();
        List<Users> receivers=group.getGroupMembers();
        for(Users receiver:receivers){
            if(onlineUsers.contains(receiver.getUsername())){
                MessageUserId messageUserId=new MessageUserId(createdMessage.getMsg_id(),receiver.getId());
                MessageDelivery messageDelivery=new MessageDelivery(messageUserId);
                messageDeliveryRepo.save(messageDelivery);
                //createdMessage.addDeliveredUsers(receiver);

            }
        }

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
        Users user=userRepo.findByUsername(username);
        Optional<GroupMessage> message=groupMessageRepo.findById(msgId);
        if(message.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        GroupMessage groupMessage=message.get();
        GroupChat groupChat=groupMessage.getGroupChat();
        if(!groupChat.getGroupMembers().contains(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("everyone") && !groupMessage.getSender().equals(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("me")){
            MessageUserId messageUserId=new MessageUserId(msgId,user.getId());
            GroupMessageDeletedUser groupMessageDeletedUser=new GroupMessageDeletedUser(messageUserId);
            groupMessageDeletedUserRepo.save(groupMessageDeletedUser);
            eventPublisher.publishEvent(new MessageDeletedForMeEvent(groupMessage.getMsg_id(),user.getUsername()));
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        else if(scope.equals("everyone")){
            groupMessage.setDeletedForEveryone(true);
            groupMessageRepo.save(groupMessage);
            System.out.println("publishing....");
            eventPublisher.publishEvent(new GroupMessageDeletedEvent(groupMessage));


        }



        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> reactEmoji(String username, long msgId, String emoji) {

        Optional<GroupMessage> msg=groupMessageRepo.findById(msgId);
        if(msg.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        GroupMessage groupMessage=msg.get();
        Users user=userRepo.findByUsername(username);
        if(!groupMessage.getGroupChat().getGroupMembers().contains(user)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Boolean exists=groupMessageRepo.existsByMsgIdAndUserId(groupMessage.getMsg_id(),user.getId());
        GroupMessageEmojiReaction groupMessageEmojiReaction;
        if(exists){
            groupMessageEmojiReaction=groupMessageRepo.findByMsgIdAndUserId(groupMessage.getMsg_id(),user.getId());
            groupMessageEmojiReaction.setEmoji(emoji);
            groupMessageEmojiReactionRepo.save(groupMessageEmojiReaction);

        }
        else{
            MessageUserId messageUserId=new MessageUserId(groupMessage.getMsg_id(),user.getId());
            groupMessageEmojiReaction=new GroupMessageEmojiReaction(messageUserId,emoji);
            groupMessageEmojiReactionRepo.save(groupMessageEmojiReaction);

        }
        Map<String,Long> emojisMap=new HashMap<>();
        List<Object[]> emojis=groupMessageRepo.findEmojisCountForMessage(groupMessage.getMsg_id());
        for(Object[] row:emojis){
            emojisMap.put((String)row[0],(Long)row[1]);
        }
        for(Map.Entry<String,Long> entry:emojisMap.entrySet()){
            System.out.println(entry.getKey()+"  "+entry.getValue());
        }
        eventPublisher.publishEvent(new GroupMessageEmojiCreatedEvent(groupMessage.getMsg_id(),emojisMap,groupMessage.getGroupChat().getId()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
