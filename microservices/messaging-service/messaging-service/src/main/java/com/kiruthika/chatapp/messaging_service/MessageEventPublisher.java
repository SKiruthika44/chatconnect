package com.kiruthika.chatapp.messaging_service;


import com.kiruthika.chatapp.messaging_service.config.RabbitMqConfig;
import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.event.*;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service

public class MessageEventPublisher   {

    private final RabbitTemplate rabbitTemplate;
    public MessageEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public  void publishDirectMessageCreated(DirectMessageCreatedEvent directMessageCreatedEvent){
        //DirectMessageResponseDto dto=directMessageCreatedEvent.getDirectMessageResponseDto();
        rabbitTemplate.convertAndSend("chat.exchange","message.created",directMessageCreatedEvent);

    }



    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void publishDirectMessageStatusUpdated(DirectMessageStatusUpdatedEvent event) {
        rabbitTemplate.convertAndSend("chat.exchange","message.status.updated",event);

    }

    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void publishDirectMessageEdited(DirectMessageEditedEvent event) {
        rabbitTemplate.convertAndSend("chat.exchange","message.edited",event);

    }

    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void publishDirectMessageDeletedForMe(MessageDeletedForMeEvent event) {
        rabbitTemplate.convertAndSend("chat.exchange","message.delete.for.me",event);

    }



    @TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void publishDirectMessageDeletedForEveryone(DirectMessageDeletedForEveryoneEvent event){

        rabbitTemplate.convertAndSend("chat.exchange","message.delete.for.everyone",event);
    }




    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void directMessageEmojiCreated(DirectMessageEmojiCreatedEvent event) {
        rabbitTemplate.convertAndSend("chat.exchange","message.emoji.created",event);
    }


}
