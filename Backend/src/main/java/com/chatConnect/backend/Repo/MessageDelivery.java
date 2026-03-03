package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageUserId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity

public class MessageDelivery {
    @Id
    private MessageUserId id;
    private LocalDateTime deliveredAt;

    public MessageDelivery(MessageUserId id) {
        this.id = id;
    }

    public MessageDelivery() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    @PrePersist

    public void setDeliveredAt() {
        this.deliveredAt = LocalDateTime.now();
    }
}
