package com.chatConnect.backend.Service;

import com.chatConnect.backend.Modal.*;
import com.chatConnect.backend.Repo.ChatMessageRepo;

import com.chatConnect.backend.Repo.UserRepo;
import com.chatConnect.backend.Config.PresenceEventListener;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

        List<ChatMessage> allMessages=new ArrayList<>();
        List<ChatMessage> messages1=chatRepo.findBySenderAndReceiver(user,otherUser,sortOption);
        List<ChatMessage> messages2=chatRepo.findBySenderAndReceiver(otherUser,user,sortOption);
        allMessages.addAll(messages1);
        allMessages.addAll(messages2);
        allMessages.sort(Comparator.comparing(ChatMessage::getCreatedAt));
        for(ChatMessage chatMessage:allMessages){
            ChatMessageResponseDTO responseMsg=new ChatMessageResponseDTO(chatMessage.getId(),chatMessage.getContent(),chatMessage.getSender().getUsername(),chatMessage.getReceiver().getUsername(),chatMessage.getCreatedAt(),"SENT");
            if(chatMessage.isDelivered()){
                responseMsg.setStatus("DELIVERED");
            }
            if(chatMessage.isRead()){
                responseMsg.setStatus("READ");
            }
            responses.add(responseMsg);
        }
        return responses;





    }


    public ResponseEntity<Void> deleteMessage(String username, long msgId,String scope) {

        /*Optional<ChatMessage> chatMessage=chatRepo.findById(msgId);
        Users user=userRepo.findByUsername(username);
        if(chatMessage.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ChatMessage msg=chatMessage.get();
        if(!msg.getSender().getUsername().equals(username) && !msg.getReceiver().getUsername().equals(username) ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("everyone") && msg.getSender().getUsername().equals(username)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        if(scope.equals("everyone")){
            chatRepo.deleteById(msgId);
            return ResponseEntity.status(HttpStatus.OK).build();

        }
        else if(scope.equals("me")){
            PrivateMessageDeletedUsers deletedUser=new PrivateMessageDeletedUsers(msg.getId(),user.getId());
            privateMessageDeletedUsersRepo.save(deletedUser);

        }*/

        return ResponseEntity.status(HttpStatus.OK).build();

    }
}
