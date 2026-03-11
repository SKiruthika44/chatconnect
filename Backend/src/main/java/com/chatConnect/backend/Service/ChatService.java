package com.chatConnect.backend.Service;

import com.chatConnect.backend.Event.*;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.*;

import com.chatConnect.backend.Config.PresenceEventListener;

import com.chatConnect.backend.exception.ForbiddenActionException;
import com.chatConnect.backend.exception.MessageNotFoundException;
import com.chatConnect.backend.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service

public class ChatService {


    @Autowired

    private ChatMessageRepo chatRepo;

    @Autowired
    private MessageDeliveryRepo messageDeliveryRepo;

    @Autowired
    private MessageReadRepo messageReadRepo;

    @Autowired
    private GroupService groupService;

    @Autowired
    private LanguageDetectionService languageDetectionService;

    @Autowired

    private UserRepo userRepo;

    @Autowired

    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PresenceEventListener presenceEventListener;

    @Autowired
    private PrivateMessageDeletedUserRepo privateMessageDeletedUserRepo;

    @Autowired
    private PrivateMessageEmojiReactionRepo privateMessageEmojiReactionRepo;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Transactional
    public ResponseEntity<Void> sendMessage(String username, ChatMessageDTO msg) {

        Users sender=userRepo.findByUsername(username);
        if(msg.getContent()==null || msg.getReceiver()==null){
            throw new IllegalArgumentException("content or receiver missing");
        }
        Set<String> onlineUsers=presenceEventListener.getOnlineUsers();

            Users receiver=userRepo.findByUsername(msg.getReceiver());
            String lang=languageDetectionService.detectLanguage(msg.getContent());
           ChatMessage chatMessage=new ChatMessage(sender,receiver,msg.getContent(),lang);
            chatRepo.save(chatMessage);

            //add sender to delivered and read
        MessageUserId senderMessageUserId=new MessageUserId(chatMessage.getMsg_id(), sender.getId());
        MessageDelivery senderMessageDelivery=new MessageDelivery(senderMessageUserId);
        messageDeliveryRepo.save(senderMessageDelivery);
        MessageRead messageRead=new MessageRead(senderMessageUserId);
        messageReadRepo.save(messageRead);

        ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getMsg_id(),chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
            String status="SENT";
            if(onlineUsers.contains(receiver.getUsername())){

                MessageUserId messageUserId=new MessageUserId(chatMessage.getMsg_id(), receiver.getId());
                MessageDelivery messageDelivery=new MessageDelivery(messageUserId);
                messageDeliveryRepo.save(messageDelivery);

                responseMsg.setStatus("DELIVERED");
                status="DELIVERED";
               // simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/private",responseMsg);

            }
            eventPublisher.publishEvent(new PrivateMessageCreatedEvent(chatMessage,status));

            //simpMessagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/private", responseMsg);

            return ResponseEntity.status(HttpStatus.CREATED).build();



    }

    public ResponseEntity<Map<String,Integer>> getUnReadCounts(String username) {

       Users user=userRepo.findByUsername(username);

        Map<String,Integer> unReadCounts=new HashMap<>();
        List<ChatMessage> allMessages=chatRepo.findByReceiver(user);
        for(ChatMessage chatMessage:allMessages){
            boolean exists=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(),user.getId());
            if(!exists){
                String sender=chatMessage.getSender().getUsername();
                unReadCounts.put(sender, unReadCounts.getOrDefault(sender,0)+1);
            }

        }
        System.out.println("unreadcount"+unReadCounts);


       return ResponseEntity.status(HttpStatus.OK).body(unReadCounts);

    }

    public ResponseEntity<Void> markMessagesRead(String username, String sendername) {

        Users receiver=userRepo.findByUsername(username);
        Users sender=userRepo.findByUsername(sendername);
        List<ChatMessage> unReadMessages=new ArrayList<>();
        //List<ChatMessage> allMessages=chatRepo.findByReceiverAndSenderAndIsReadFalse(receiver,sender);
        List<ChatMessage> allMessages=chatRepo.findByReceiverAndSender(receiver,sender);
        for(ChatMessage chatMessage:allMessages){
            boolean exists=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
            if(!exists){
                unReadMessages.add(chatMessage);
            }
        }
        System.out.println("allMessages"+allMessages);
        for(ChatMessage chatMessage:unReadMessages){

            MessageUserId messageUserId=new MessageUserId(chatMessage.getMsg_id(), receiver.getId());
            MessageRead messageRead=new MessageRead(messageUserId);
            messageReadRepo.save(messageRead);

            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getMsg_id(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"READ");

            simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/private",responseMsg);
        }
        return ResponseEntity.status(HttpStatus.OK).build();



    }

    public ResponseEntity<Void> markMessageRead(String username, long msgId) {

        Users user=userRepo.findByUsername(username);

        Optional<ChatMessage> optionalMessage=chatRepo.findById(msgId);
        if(optionalMessage.isPresent()){
            ChatMessage chatMessage=optionalMessage.get();

            if(!chatMessage.getSender().equals(user)){ //sender cannot update read
                MessageUserId messageUserId=new MessageUserId(chatMessage.getMsg_id(), user.getId());
                MessageRead messageRead=new MessageRead(messageUserId);
                messageReadRepo.save(messageRead);
                ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getMsg_id(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"READ");
                simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/private",responseMsg);
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            else{
                throw new ForbiddenActionException("Sender cannot update read status");
            }
        }
        else{
            System.out.println("Message not found");
            throw new MessageNotFoundException("Message not found for this id");
        }

    }

    public ResponseEntity<List<ChatMessageResponseDTO>> getAllMessagesBetweenSenderAndReceiver(UserPrincipal userPrincipal, String otherUsername) {
        List<ChatMessageResponseDTO> responses=new ArrayList<>();
        String username=userPrincipal.getUsername();
        System.out.println(username);
        Users user=userRepo.findByUsername(username);
        Users otherUser=userRepo.findByUsername((otherUsername));
        Sort sortOption=Sort.by(Sort.Direction.ASC,"createdAt");


        List<ChatMessage> allMessages = new ArrayList<>(chatRepo.findAllVisibleMessageBetweenSenderAndReceiver(user, otherUser, user.getId()));
        for(ChatMessage chatMessage:allMessages){
            List<String> emojis=chatRepo.findEmojisByMessageId(chatMessage.getMsg_id());
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getMsg_id(),chatMessage.getContent(),chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
            Users receiver=chatMessage.getReceiver();
            boolean delivered=messageDeliveryRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
            if(delivered){
                responseMsg.setStatus("DELIVERED");
            }
            boolean read=messageReadRepo.existsByMsgIdAndUserIdByCount(chatMessage.getMsg_id(), receiver.getId());
            if(read){
                responseMsg.setStatus("READ");
            }


            if(chatMessage.isDeletedForEveryone()){
                responseMsg.setDeletedForEveryone(true);
                responseMsg.setContent(null);
            }
            if(emojis.size()>0){
                responseMsg.setEmojis(emojis);

            }

            responses.add(responseMsg);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responses);





    }


    public ResponseEntity<Void> deleteMessage(String username, long msgId,String scope) {
        Users user=userRepo.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        if(!scope.equals("everyone") && !scope.equals("me")){
            throw new IllegalArgumentException("Invalid delete scope");
        }
        System.out.println("scope="+scope);
       ChatMessage msg=chatRepo.findById(msgId).orElseThrow(()-> new MessageNotFoundException("Message not found"));



        if(!msg.getSender().getUsername().equals(username) && !msg.getReceiver().getUsername().equals(username) ){
            throw new ForbiddenActionException("Only sender and receiver can delete");
        }
        if(scope.equals("everyone") && !msg.getSender().getUsername().equals(username)){
            throw new ForbiddenActionException("Only sender can delete for everyone");

        }
        if(scope.equals("everyone")){

            msg.setDeletedForEveryone(true);
            chatRepo.save(msg);
            eventPublisher.publishEvent(new PrivateMessageDeletedEvent(msg));


        }
        else {
            MessageUserId messageUserId=new MessageUserId(msg.getMsg_id(),user.getId());
            PrivateMessageDeletedUser deletedUser=new PrivateMessageDeletedUser(messageUserId);

            privateMessageDeletedUserRepo.save(deletedUser);
            eventPublisher.publishEvent(new MessageDeletedForMeEvent(msg.getMsg_id(),user.getUsername()));

        }
        return ResponseEntity.status(HttpStatus.OK).build();



    }

    public ResponseEntity<Void> reactEmoji(String username, long msgId, String emoji) {
        Users user=userRepo.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("User not found");
        }
        ChatMessage chatMessage=chatRepo.findById(msgId).orElseThrow(()->new MessageNotFoundException("Message Not found"));


        if(!chatMessage.getSender().getUsername().equals(username) && !chatMessage.getReceiver().getUsername().equals(username)){
            throw new ForbiddenActionException("Not allowed");
        }

        if(emoji==null || emoji.isBlank()){
           throw new IllegalArgumentException("emoji is blank");

        }

        Boolean exists=chatRepo.existsMessageUserId(chatMessage.getMsg_id(),user.getId());
        PrivateMessageEmojiReaction privateMessageEmojiReaction;
        if(!exists){
            MessageUserId messageUserId=new MessageUserId(chatMessage.getMsg_id(),user.getId());
            privateMessageEmojiReaction=new PrivateMessageEmojiReaction(messageUserId,emoji);
            privateMessageEmojiReactionRepo.save(privateMessageEmojiReaction);

        }
        else{
            privateMessageEmojiReaction=chatRepo.findByMsgIdAndUserId(msgId,user.getId());
            privateMessageEmojiReaction.setEmoji(emoji);
            privateMessageEmojiReactionRepo.save(privateMessageEmojiReaction);
        }
        List<String> emojis=chatRepo.findEmojisByMessageId(chatMessage.getMsg_id());

        eventPublisher.publishEvent(new EmojiCreatedEvent(chatMessage.getMsg_id(),chatMessage.getSender()
                .getUsername(),chatMessage.getReceiver().getUsername(),emojis));

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        Users user=userRepo.findByUsername(username);
        if(user==null){
            throw new UserNotFoundException("user not found");
        }
        if(content==null || content.isBlank()){
            throw new IllegalArgumentException("Content is blank");
        }
        ChatMessage chatMessage=chatRepo.findById(msgId).orElseThrow(()->new MessageNotFoundException("Message not found"));

        if(!chatMessage.getSender().getUsername().equals(user.getUsername())){//only sender can edit the msg
           throw new ForbiddenActionException("Only sender can edit the message");
        }
        chatMessage.setContent(content);
        chatRepo.save(chatMessage);
        eventPublisher.publishEvent(new MessageEditedEvent(chatMessage));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
