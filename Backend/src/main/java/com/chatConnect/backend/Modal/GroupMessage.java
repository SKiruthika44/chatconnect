package com.chatConnect.backend.Modal;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GroupMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="sender-id")

    private Users sender;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="group-id")

    private GroupChat groupChat;

    private String content;


    private String detectedLanguage;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(name="delivered-users",
    joinColumns = @JoinColumn(name="groupmessage-id"),
    inverseJoinColumns = @JoinColumn(name="user-id"))

    private List<Users> deliveredUsers;


    @ManyToMany
    @JoinTable(name="read-users",
            joinColumns = @JoinColumn(name="groupmessage-id"),
            inverseJoinColumns = @JoinColumn(name="user-id"))

    private List<Users> readUsers;

    public GroupMessage() {
    }

    public GroupMessage(Users sender, GroupChat groupChat, String content,String detectedLanguage) {

        this.sender = sender;
        this.groupChat = groupChat;
        this.content = content;
        this.detectedLanguage=detectedLanguage;

        this.deliveredUsers = new ArrayList<>();
        this.readUsers = new ArrayList<>();
    }

    @PrePersist

    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
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

    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
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



    public List<Users> getDeliveredUsers() {
        return deliveredUsers;
    }

    public void setDeliveredUsers(List<Users> deliveredUsers) {
        this.deliveredUsers = deliveredUsers;
    }

    public List<Users> getReadUsers() {
        return readUsers;
    }

    public void setReadUsers(List<Users> readUsers) {
        this.readUsers = readUsers;
    }

    public void addDeliveredUsers(Users sender) {
        this.deliveredUsers.add(sender);
    }

    public void addReadUsers(Users sender) {
        this.readUsers.add(sender);
    }

    public String getDetectedLanguage() {
        return detectedLanguage;
    }

    public void setDetectedLanguage(String detectedLanguage) {
        this.detectedLanguage = detectedLanguage;
    }
}
