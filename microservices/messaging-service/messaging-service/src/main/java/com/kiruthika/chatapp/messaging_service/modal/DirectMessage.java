package com.kiruthika.chatapp.messaging_service.modal;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DIRECT")
@Data
@AllArgsConstructor

@NoArgsConstructor
public class DirectMessage extends Message{

    private Long receiverId;

}
