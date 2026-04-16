package com.kiruthika.chatapp.ws_service.listener;

import com.kiruthika.chatapp.ws_service.dto.GroupResponseDto;
import com.kiruthika.chatapp.ws_service.event.GroupCreatedEvent;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GroupEventListener {
    private final SimpMessagingTemplate simpMessagingTemplate;


    @RabbitListener(queues = "ws.group.created.queue")
    public void groupCreatedEvenet(GroupCreatedEvent groupCreatedEvent){
        GroupResponseDto dto= groupCreatedEvent.getGroupResponseDto();
        for(String member:dto.getGroupMembers()){
            simpMessagingTemplate.convertAndSendToUser(member,"/queue/groupInfo",dto);

        }
    }
}
