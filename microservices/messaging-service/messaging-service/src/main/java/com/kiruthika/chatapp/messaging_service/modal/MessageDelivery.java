package com.kiruthika.chatapp.messaging_service.modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

public class MessageDelivery {
    @Id
    private MessageUserId id;
    private LocalDateTime deliveredAt;

}
