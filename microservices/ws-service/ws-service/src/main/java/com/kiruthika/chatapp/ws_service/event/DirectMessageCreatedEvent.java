package com.kiruthika.chatapp.ws_service.event;

import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectMessageCreatedEvent implements Serializable {
    private DirectMessageResponseDto directMessageResponseDto;
}
