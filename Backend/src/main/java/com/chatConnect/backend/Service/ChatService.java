package com.chatConnect.backend.Service;

import com.chatConnect.backend.Event.EmojiCreatedEvent;
import com.chatConnect.backend.Event.PrivateMessageDeletedEvent;
import com.chatConnect.backend.Event.MessageDeletedForMeEvent;
import com.chatConnect.backend.Event.PrivateMessageEditedEvent;
import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;

import com.chatConnect.backend.Repo.PrivateMessageDeletedUserRepo;
import com.chatConnect.backend.Repo.PrivateMessageEmojiReactionRepo;
import com.chatConnect.backend.Repo.UserRepo;
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
        ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(),chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");

            if(onlineUsers.contains(receiver.getUsername())){
                chatMessage.setDelivered(true);
                responseMsg.setStatus("DELIVERED");
                simpMessagingTemplate.convertAndSendToUser(receiver.getUsername(),"/queue/private",responseMsg);

            }

            simpMessagingTemplate.convertAndSendToUser(sender.getUsername(), "/queue/private", responseMsg);





    }

    public Map<String,Integer> getUnReadCounts(String username) {

       Users user=userRepo.findByUsername(username);
       List<ChatMessage> allMessages=chatRepo.findByReceiverAndIsReadFalse(user);

       Map<String,Integer> unReadCounts=new HashMap<>();
       for(ChatMessage msg:allMessages){
           String sender=msg.getSender().getUsername();
           unReadCounts.put(sender, unReadCounts.getOrDefault(sender,0)+1);
       }
       return unReadCounts;

    }

    public void markMessagesRead(String username, String sendername) {

        Users receiver=userRepo.findByUsername(username);
        Users sender=userRepo.findByUsername(sendername);
        List<ChatMessage> allMessages=chatRepo.findByReceiverAndSenderAndIsReadFalse(receiver,sender);

        System.out.println("allMessages"+allMessages);
        for(ChatMessage chatMessage:allMessages){
            chatMessage.setRead(true);
            chatRepo.save(chatMessage);
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"READ");

            simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/private",responseMsg);
        }



    }

    public void markMessageRead(String username, long msgId) {

        Users user=userRepo.findByUsername(username);

        Optional<ChatMessage> optionalMessage=chatRepo.findById(msgId);
        if(optionalMessage.isPresent()){
            ChatMessage chatMessage=optionalMessage.get();
            chatMessage.setRead(true);
            chatRepo.save(chatMessage);
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(), chatMessage.getContent(), chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"READ");
            simpMessagingTemplate.convertAndSendToUser(chatMessage.getSender().getUsername(),"/queue/private",responseMsg);
        }

    }

    public List<ChatMessageResponseDTO> getAllMessagesBetweenSenderAndReceiver(UserPrincipal userPrincipal, String otherUsername) {
        List<ChatMessageResponseDTO> responses=new ArrayList<>();
        String username=userPrincipal.getUsername();
        System.out.println(username);
        Users user=userRepo.findByUsername(username);
        Users otherUser=userRepo.findByUsername((otherUsername));
        Sort sortOption=Sort.by(Sort.Direction.ASC,"createdAt");

        /*List<ChatMessage> messages1=chatRepo.findBySenderAndReceiver(user,otherUser,sortOption);
        List<ChatMessage> messages2=chatRepo.findBySenderAndReceiver(otherUser,user,sortOption);
        allMessages.addAll(messages1);
        allMessages.addAll(messages2);
        allMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt));*/
        List<ChatMessage> allMessages = new ArrayList<>(chatRepo.findAllVisibleMessageBetweenSenderAndReceiver(user, otherUser, user.getId()));
        for(ChatMessage chatMessage:allMessages){
            List<String> emojis=chatRepo.findEmojisByMessageId(chatMessage.getId());
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(),chatMessage.getContent(),chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
            if(chatMessage.isDelivered()){
                responseMsg.setStatus("DELIVERED");
            }
            if(chatMessage.isRead()){
                responseMsg.setStatus("READ");
            }
            if(chatMessage.isDeletedForEveryOne()){
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
            msg.setDeletedForEveryOne(true);
            chatRepo.save(msg);
            eventPublisher.publishEvent(new PrivateMessageDeletedEvent(msg));
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        else if(scope.equals("me")){
            MessageUserId messageUserId=new MessageUserId(msg.getId(),user.getId());
            PrivateMessageDeletedUser deletedUser=new PrivateMessageDeletedUser(messageUserId);

            privateMessageDeletedUserRepo.save(deletedUser);
            eventPublisher.publishEvent(new MessageDeletedForMeEvent(msg.getId(),user.getUsername()));

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
        Boolean exists=chatRepo.existsMessageUserId(chatMessage.getId(),user.getId());
        PrivateMessageEmojiReaction privateMessageEmojiReaction;
        if(!exists){
            MessageUserId messageUserId=new MessageUserId(chatMessage.getId(),user.getId());
            privateMessageEmojiReaction=new PrivateMessageEmojiReaction(messageUserId,emoji);
            privateMessageEmojiReactionRepo.save(privateMessageEmojiReaction);

        }
        else{
            privateMessageEmojiReaction=chatRepo.findByMsgIdAndUserId(msgId,user.getId());
            privateMessageEmojiReaction.setEmoji(emoji);
            privateMessageEmojiReactionRepo.save(privateMessageEmojiReaction);
        }
        List<String> emojis=chatRepo.findEmojisByMessageId(chatMessage.getId());
        for(String e:emojis){
            System.out.println("emoji:"+e);
        }
        eventPublisher.publishEvent(new EmojiCreatedEvent(chatMessage.getId(),chatMessage.getSender()
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
        if(!chatMessage.getSender().getUsername().equals(user.getUsername()) && !chatMessage.getReceiver().getUsername().equals(user.getUsername())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        chatMessage.setContent(content);
        chatRepo.save(chatMessage);
        eventPublisher.publishEvent(new PrivateMessageEditedEvent(chatMessage));
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
