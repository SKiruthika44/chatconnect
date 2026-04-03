package com.kiruthika.chatapp.messaging_service.modal;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class MessageDeletion {
    @Id
    private MessageUserId id;
    private LocalDateTime deletedAt;

}
