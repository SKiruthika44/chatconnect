package com.kiruthika.chatapp.messaging_service.modal;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageUserId implements Serializable {
    private long messageId;
    private long userId;

}
