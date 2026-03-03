package com.chatConnect.backend.Modal;


import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("GROUP")
public class GroupMessage extends Message {



    @ManyToOne
    @JoinColumn(name="group-id")

    private GroupChat groupChat;



    public GroupMessage() {
    }

    public GroupMessage(Users sender, GroupChat groupChat, String content,String detectedLanguage) {
        super(content,detectedLanguage,sender);


        this.groupChat = groupChat;

    }



    public GroupChat getGroupChat() {
        return groupChat;
    }

    public void setGroupChat(GroupChat groupChat) {
        this.groupChat = groupChat;
    }
}
