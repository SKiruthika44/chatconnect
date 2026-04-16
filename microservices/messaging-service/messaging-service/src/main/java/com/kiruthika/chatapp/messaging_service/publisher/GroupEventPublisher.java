package com.kiruthika.chatapp.messaging_service.publisher;


import com.kiruthika.chatapp.messaging_service.event.GroupCreatedEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class GroupEventPublisher {
    private final RabbitTemplate rabbitTemplate;
    public GroupEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishGroupCreatedEvent(GroupCreatedEvent groupCreatedEvent){
        rabbitTemplate.convertAndSend("chat.exchange","group.created",groupCreatedEvent);
    }
}
