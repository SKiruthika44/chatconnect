package com.kiruthika.chatapp.messaging_service.publisher;

import com.kiruthika.chatapp.messaging_service.event.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class GroupMessageEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    public GroupMessageEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void groupMessageCreated(GroupMessageCreatedEvent groupMessageCreatedEvent){
        rabbitTemplate.convertAndSend("chat.exchange","group.message.created",groupMessageCreatedEvent);


    }

    public void groupMessageStatusUpdated(GroupMessageStatusUpdatedEvent groupMessageStatusUpdatedEvent) {

        rabbitTemplate.convertAndSend("chat.exchange","group.message.status.updated",groupMessageStatusUpdatedEvent);
    }

    public void groupMessageEdited(GroupMessageEditedEvent groupMessageEditedEvent){

        rabbitTemplate.convertAndSend("chat.exchange","group.message.edited",groupMessageEditedEvent);
    }

    public void groupMessageDeletedForEveryone(GroupMessageDeletedForEveryoneEvent event){

        rabbitTemplate.convertAndSend("chat.exchange","group.message.delete.for.everyone",event);
    }

    public void groupMessageEmojiCreated(GroupMessageEmojiCreatedEvent event){
        rabbitTemplate.convertAndSend("chat.exchange","group.message.emoji.created",event);
    }
}
