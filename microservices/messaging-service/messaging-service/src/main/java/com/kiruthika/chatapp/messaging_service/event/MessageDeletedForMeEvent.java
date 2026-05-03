package com.kiruthika.chatapp.messaging_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class MessageDeletedForMeEvent {

    private long msgId;
    private String username;
}
