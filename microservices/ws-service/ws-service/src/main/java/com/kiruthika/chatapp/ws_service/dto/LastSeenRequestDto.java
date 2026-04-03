package com.kiruthika.chatapp.ws_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LastSeenRequestDto {
    private LocalDateTime lastseen;
    private String username;
}
