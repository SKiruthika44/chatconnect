package com.kiruthika.chatapp.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserRequestDTO {
    private String username;
    private String password;

}
