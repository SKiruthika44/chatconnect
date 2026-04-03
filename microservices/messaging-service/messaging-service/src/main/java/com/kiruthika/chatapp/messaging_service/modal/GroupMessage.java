package com.kiruthika.chatapp.messaging_service.modal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("GROUP")
@Data
@AllArgsConstructor

@NoArgsConstructor

public class GroupMessage extends Message{
    private Long groupId;

}
