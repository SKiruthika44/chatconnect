package com.kiruthika.chatapp.ws_service.event;


import com.kiruthika.chatapp.ws_service.dto.DirectMessageResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DirectMessageEmojiCreatedEvent {
    private DirectMessageResponseDto directMessageResponseDto;
}
