package com.chatConnect.backend.Modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity

public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(unique = true,nullable=false)
    private String username;
    private String password;

    private LocalDateTime lastSeen=LocalDateTime.now().minusDays(1);


    private String profileImage;
    private String preferredLanguage;

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public Users(){

    }

    public Users(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", lastSeen=" + lastSeen +
                ", profileImage='" + profileImage + '\'' +
                ", preferredLanguage='" + preferredLanguage + '\'' +
                '}';
    }
}
