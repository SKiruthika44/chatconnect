package com.chatConnect.backend.Modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity

public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name="sender-id",nullable=false)
    private Users sender;

    @ManyToOne
    @JoinColumn(name="receiver-id")
    private Users receiver;

    @Column(nullable = false)

    private String content;




    private LocalDateTime createdAt;

    private boolean delivered=false;
    private boolean isRead=false;

    private String detectedLanguage;
    private boolean isDeletedForEveryOne=false;

    private LocalDateTime deletedAt;








    public ChatMessage( Users sender, Users receiver, String content,String detectedLanguage) {

        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.detectedLanguage=detectedLanguage;


    }

    public ChatMessage(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Users getSender() {
        return sender;
    }

    public void setSender(Users sender) {
        this.sender = sender;
    }

    public Users getReceiver() {
        return receiver;
    }


    @PrePersist
    protected void OnCreate(){
        this.createdAt=LocalDateTime.now();
    }
    public void setReceiver(Users receiver) {
        this.receiver = receiver;
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


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", sender=" + sender.getUsername() +

                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", delivered=" + delivered + "isRead" + isRead +
                '}';
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }

    public boolean isDeletedForEveryOne() {
        return isDeletedForEveryOne;
    }

    public void setDeletedForEveryOne(boolean deletedForEveryOne) {
        isDeletedForEveryOne = deletedForEveryOne;
        this.deletedAt=LocalDateTime.now();
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
