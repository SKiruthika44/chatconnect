package com.chatConnect.backend.Modal;

import java.time.LocalDateTime;
import java.util.List;

public class GroupResponseDTO {
    private long id;

    private String groupName;

    private String admin;

    private List<String> groupMembers;

    private LocalDateTime createdAt;

    public GroupResponseDTO(long id, String groupName, String admin, List<String> groupMembers, LocalDateTime createdAt) {
        this.id = id;
        this.groupName = groupName;
        this.admin = admin;
        this.groupMembers = groupMembers;
        this.createdAt = createdAt;
    }

    public GroupResponseDTO() {




    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<String> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "GroupResponseDTO{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                ", admin='" + admin + '\'' +
                ", groupMembers=" + groupMembers +
                ", createdAt=" + createdAt +
                '}';
    }
}
