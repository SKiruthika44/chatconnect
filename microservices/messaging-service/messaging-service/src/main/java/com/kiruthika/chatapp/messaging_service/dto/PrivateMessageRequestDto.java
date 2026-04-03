package com.kiruthika.chatapp.messaging_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PrivateMessageRequestDto {
    private String receiver;
    private String content;

}
