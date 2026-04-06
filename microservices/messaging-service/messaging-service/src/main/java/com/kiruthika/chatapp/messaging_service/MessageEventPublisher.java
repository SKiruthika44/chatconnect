package com.kiruthika.chatapp.messaging_service;


import com.kiruthika.chatapp.messaging_service.config.RabbitMqConfig;
import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import com.kiruthika.chatapp.messaging_service.event.DirectMessageCreatedEvent;
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
        System.out.println("Message sent: " + System.currentTimeMillis());
    }




}
