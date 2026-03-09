package com.chatConnect.backend.Service;

import com.chatConnect.backend.Event.EmojiCreatedEvent;
import com.chatConnect.backend.Event.PrivateMessageDeletedEvent;
import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import com.chatConnect.backend.Event.MessageEditedEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.*;

import com.chatConnect.backend.Config.PresenceEventListener;

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
    public void sendMessage(String username, ChatMessageDTO msg) {

        Users sender=userRepo.findByUsername(username);
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

            if(onlineUsers.contains(receiver.getUsername())){

                MessageUserId messageUserId=new MessageUserId(chatMessage.getMsg_id(), receiver.getId());
                MessageDelivery messageDelivery=new MessageDelivery(messageUserId);
                messageDeliveryRepo.save(messageDelivery);

                responseMsg.setStatus("DELIVERED");
                simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/private",responseMsg);

            }

            simpMessagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/private", responseMsg);





    }

    public Map<String,Integer> getUnReadCounts(String username) {

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


       return unReadCounts;

    }

    public void markMessagesRead(String username, String sendername) {

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



    }

    public void markMessageRead(String username, long msgId) {

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
            }
        }

    }

    public List<ChatMessageResponseDTO> getAllMessagesBetweenSenderAndReceiver(UserPrincipal userPrincipal, String otherUsername) {
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
        return responses;





    }


    public ResponseEntity<Void> deleteMessage(String username, long msgId,String scope) {
        System.out.println("scope="+scope);
        Optional<ChatMessage> chatMessage=chatRepo.findById(msgId);
        Users user=userRepo.findByUsername(username);
        if(chatMessage.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ChatMessage msg=chatMessage.get();
        if(!msg.getSender().getUsername().equals(username) && !msg.getReceiver().getUsername().equals(username) ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("everyone") && !msg.getSender().getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("everyone")){
            System.out.println("everyone");
            msg.setDeletedForEveryone(true);
            chatRepo.save(msg);
            eventPublisher.publishEvent(new PrivateMessageDeletedEvent(msg));
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        else if(scope.equals("me")){
            MessageUserId messageUserId=new MessageUserId(msg.getMsg_id(),user.getId());
            PrivateMessageDeletedUser deletedUser=new PrivateMessageDeletedUser(messageUserId);

            privateMessageDeletedUserRepo.save(deletedUser);
            eventPublisher.publishEvent(new MessageDeletedForMeEvent(msg.getMsg_id(),user.getUsername()));

        }

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    public ResponseEntity<Void> reactEmoji(String username, long msgId, String emoji) {
        System.out.println("request came in chatservice");
        Optional<ChatMessage> msg=chatRepo.findById(msgId);
        if(msg.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ChatMessage chatMessage=msg.get();
        if(!chatMessage.getSender().getUsername().equals(username) && !chatMessage.getReceiver().getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Users user=userRepo.findByUsername(username);
        if(emoji.equals("")){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        }
        System.out.println("request processing");
        System.out.println("emoji="+emoji);
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
        for(String e:emojis){
            System.out.println("emoji:"+e);
        }
        eventPublisher.publishEvent(new EmojiCreatedEvent(chatMessage.getMsg_id(),chatMessage.getSender()
                .getUsername(),chatMessage.getReceiver().getUsername(),emojis));
        System.out.println("emoji="+privateMessageEmojiReaction.getEmoji());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity<Void> editMessage(String username, long msgId, String content) {
        Users user=userRepo.findByUsername(username);
        Optional<ChatMessage> msg=chatRepo.findById(msgId);
        if(msg.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ChatMessage chatMessage=msg.get();
        if(!chatMessage.getSender().getUsername().equals(user.getUsername())){//only sender can edit the msg
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        chatMessage.setContent(content);
        chatRepo.save(chatMessage);
        eventPublisher.publishEvent(new MessageEditedEvent(chatMessage));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
