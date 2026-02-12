package com.chatConnect.backend.Modal;

import java.time.LocalDateTime;

public class ChatMessageResponseDTO {

    long id;

    String content;
    private String senderName;
    private String receiverName;
    private LocalDateTime createdAt;


    private String status;
    public ChatMessageResponseDTO(long id,String content, String senderName, String receiverName, LocalDateTime createdAt,String status) {
        this.id=id;
        this.content = content;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.createdAt = createdAt;
        this.status=status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }



    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
