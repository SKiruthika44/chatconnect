package com.kiruthika.chatapp.ws_service.listener;


import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.ws_service.event.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor

public class MessageEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @RabbitListener(queues = "ws.message.created.queue")
    public void directMessageCreatedEvent(DirectMessageCreatedEvent event){

        DirectMessageResponseDto dto=event.getDirectMessageResponseDto();

        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/private",dto);

        simpMessagingTemplate.convertAndSendToUser(dto.getReceiverName(),"/queue/private",dto);

    }

    @RabbitListener(queues = "ws.message.status.queue")
    public void directMessageStatusUpdatedEvent(DirectMessageStatusUpdatedEvent event){

        DirectMessageResponseDto dto=event.getDirectMessageResponseDto();
        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/private",dto);
    }

    @RabbitListener(queues = "ws.message.edited.queue")
    public void directMessageEditedEvent(DirectMessageStatusUpdatedEvent event){

        DirectMessageResponseDto dto=event.getDirectMessageResponseDto();
        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/message/edit",dto);
        simpMessagingTemplate.convertAndSendToUser(dto.getReceiverName(),"/queue/message/edit",dto);


    }

    @RabbitListener(queues = "ws.delete.for.me.queue")
    public void messageDeletedForMe(MessageDeletedForMeEvent event){
        simpMessagingTemplate.convertAndSendToUser(event.getUsername(),"/queue/delete-for-me",event.getMsgId());
    }


    @RabbitListener(queues="ws.delete.for.everyone.queue")
    public void messageDeletedForEveryone(DirectMessageDeletedForEveryoneEvent event){

        DirectMessageResponseDto dto=event.getDirectMessageResponseDto();

        simpMessagingTemplate.convertAndSendToUser(dto.getReceiverName(),"/queue/delete",dto);
        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/delete",dto);
    }

    @RabbitListener(queues = "ws.emoji.created.queue")
    public void emojiCreated(DirectMessageEmojiCreatedEvent event){

        DirectMessageResponseDto dto=event.getDirectMessageResponseDto();

        simpMessagingTemplate.convertAndSendToUser(dto.getReceiverName(),"/queue/private/emoji",dto);
        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/private/emoji",dto);

    }


}
