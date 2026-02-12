package com.chatConnect.backend.Modal;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserDTO {
    private long id;

    private String username;

    private LocalDateTime lastSeen;

    private String profileImage;

    private String preferredLanguage="en";

    public UserDTO(long id, String username, LocalDateTime lastSeen,String profileImage,String preferredLanguage) {
        this.id = id;
        this.username = username;
        this.lastSeen=lastSeen;
        this.profileImage=profileImage;
        this.preferredLanguage=preferredLanguage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", lastSeen=" + lastSeen +
                '}';
    }

    public LocalDateTime getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }
}
