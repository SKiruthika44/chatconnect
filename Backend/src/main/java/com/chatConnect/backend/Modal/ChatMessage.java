package com.chatConnect.backend.Modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("DIRECT")

public class ChatMessage extends Message{



    @ManyToOne
    @JoinColumn(name="receiver-id")
    private Users receiver;










    public ChatMessage( Users sender, Users receiver, String content,String detectedLanguage) {
        super(content,detectedLanguage,sender);
        this.receiver=receiver;



    }

    public ChatMessage(){

    }





    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }

    public Users getReceiver() {
        return receiver;
    }



/* public String getDetectedLanguage() {
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

    public Users getReceiver() {
        return receiver;
    }
    public void setReceiver(Users receiver) {
        this.receiver = receiver;
    }*/

}
