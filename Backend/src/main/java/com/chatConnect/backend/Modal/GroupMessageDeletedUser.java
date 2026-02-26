package com.chatConnect.backend.Modal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class GroupMessageDeletedUser {

    @Id
    private MessageUserId id;
    private LocalDateTime deletedAt;

    public GroupMessageDeletedUser(MessageUserId id) {
        this.id = id;

    }


    public GroupMessageDeletedUser() {
    }
    @PrePersist
    protected void onCreate(){
        this.deletedAt = deletedAt;
    }

    public MessageUserId getId() {
        return id;
    }

    public void setId(MessageUserId id) {
        this.id = id;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
