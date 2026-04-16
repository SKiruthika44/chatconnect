package com.kiruthika.chatapp.ws_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupMessageRequestDto {
    private String groupName;
    private String content;
}
