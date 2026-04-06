package com.kiruthika.chatapp.messaging_service.event;


import com.kiruthika.chatapp.messaging_service.dto.DirectMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class DirectMessageCreatedEvent implements Serializable {
    private DirectMessageResponseDto directMessageResponseDto;
}
