package com.kiruthika.chatapp.ws_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDeletedForMeEvent {
    private Long msgId;
    private String username;
}
