package com.kiruthika.chatapp.ws_service.listener;


import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.ws_service.event.DirectMessageCreatedEvent;
import com.kiruthika.chatapp.ws_service.event.DirectMessageStatusUpdatedEvent;
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


}
