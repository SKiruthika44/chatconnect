package com.chatConnect.backend.Modal;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String groupName;
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name="group_members",
            joinColumns=@JoinColumn(name="group_id"),
            inverseJoinColumns = @JoinColumn(name="user_id")
    )

    private List<Users> groupMembers;


    @ManyToOne
    @JoinColumn(name="admin-id")

    private Users admin;

    public GroupChat(){

    }
    public GroupChat(String name,List<Users> groupMembers,Users admin){
        this.groupName=name;
        this.groupMembers=groupMembers;
        this.admin=admin;

    }

    public long getId() {
        return id;
    }

    @PrePersist
    protected void onCreate(){
        this.createdAt=LocalDateTime.now();
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Users> getGroupMembers() {
        return groupMembers;
    }

    public void setGroupMembers(List<Users> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public Users getAdmin() {
        return admin;
    }

    public void setAdmin(Users admin) {
        this.admin = admin;
    }
}
