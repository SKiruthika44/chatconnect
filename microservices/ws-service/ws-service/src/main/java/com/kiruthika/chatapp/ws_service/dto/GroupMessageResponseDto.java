package com.kiruthika.chatapp.ws_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupMessageResponseDto {

    private long id;
    private String content;
    private LocalDateTime createdAt;

    private String status;

    private String senderName;

    private String groupName;

    private boolean deletedForEveryone=false;


    private Map<String,Long> emojisCount;

}
