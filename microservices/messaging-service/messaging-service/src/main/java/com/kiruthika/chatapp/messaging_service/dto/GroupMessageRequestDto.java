package com.kiruthika.chatapp.messaging_service.dto;

import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageRequestDto {
    private String groupName;
    private String content;
}
