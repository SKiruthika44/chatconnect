package com.kiruthika.chatapp.ws_service.listener;

import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.ws_service.dto.GroupMessageResponseDto;
import com.kiruthika.chatapp.ws_service.event.DirectMessageStatusUpdatedEvent;
import com.kiruthika.chatapp.ws_service.event.GroupMessageCreatedEvent;
import com.kiruthika.chatapp.ws_service.event.GroupMessageStatusUpdatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class GroupMessageEventListener {

    private final SimpMessagingTemplate simpMessagingTemplate;


    @RabbitListener(queues = "ws.group.message.created.queue")
    public void groupMessageCreated(GroupMessageCreatedEvent groupMessageCreatedEvent){

        GroupMessageResponseDto responseDto=groupMessageCreatedEvent.getResponseDto();
        Long groupId=groupMessageCreatedEvent.getGroupId();

        simpMessagingTemplate.convertAndSend("/topic/group/"+groupId,responseDto);

    }

    @RabbitListener(queues = "ws.group.message.status.queue")
    public void groupMessageStatusUpdatedEvent(GroupMessageStatusUpdatedEvent event){

        GroupMessageResponseDto dto=event.getGroupMessageResponseDto();
        simpMessagingTemplate.convertAndSendToUser(dto.getSenderName(),"/queue/group/"+dto.getGroupName(),dto);



    }


}
