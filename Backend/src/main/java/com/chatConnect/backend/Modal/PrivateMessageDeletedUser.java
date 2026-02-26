package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;

@Entity
public class PrivateMessageDeletedUser {
    @Id
    private MessageUserId id;
    private LocalDateTime createdAt;

    public PrivateMessageDeletedUser(MessageUserId id) {
        this.id = id;
    }

    public PrivateMessageDeletedUser() {
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    protected void onCreate(){
      this.createdAt=LocalDateTime.now();
    }
}
