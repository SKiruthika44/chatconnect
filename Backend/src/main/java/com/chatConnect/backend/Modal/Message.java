package com.chatConnect.backend.Modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="message_type")

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)


    private long msgId;


    @Column(nullable = false)
    private String content;

    private LocalDateTime createdAt;
    private boolean deletedForEveryone=false;

    private LocalDateTime deletedAt;

    private String detectedLanguage;

    @ManyToOne
    @JoinColumn(name="sender_id",nullable=false)

    private Users sender;

    public Message(String content,String detectedLanguage,Users sender){
        this.content=content;
        this.detectedLanguage=detectedLanguage;
        this.sender=sender;
    }

    public Message() {
    }

    public long getMsg_id() {
        return msgId;
    }

    public void setMsg_id(long msg_id) {
        this.msgId = msg_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }



    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public boolean isDeletedForEveryone() {
        return deletedForEveryone;
    }

    public void setDeletedForEveryone(boolean deletedForEveryone) {
        this.deletedForEveryone = deletedForEveryone;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }


    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }
}
