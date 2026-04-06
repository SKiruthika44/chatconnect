package com.kiruthika.chatapp.messaging_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DirectMessageResponseDto implements Serializable {
    private long id;

    private String content;
    private String senderName;
    private String receiverName;
    private LocalDateTime createdAt;
    private boolean deletedForEveryone=false;


    private String status;

    private List<String> emojis;

}
