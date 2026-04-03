package com.kiruthika.chatapp.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor

public class LastSeenRequestDto {
    private LocalDateTime lastseen;
    private String username;
}
