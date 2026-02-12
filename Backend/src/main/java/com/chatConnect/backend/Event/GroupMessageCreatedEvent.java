package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.GroupMessage;

public class GroupMessageCreatedEvent {

    GroupMessage groupMessage;
    public GroupMessageCreatedEvent(GroupMessage msg){
        this.groupMessage=msg;
    }

    public GroupMessage getGroupMessage() {
        return groupMessage;
    }

    public void setGroupMessage(GroupMessage groupMessage) {
        this.groupMessage = groupMessage;
    }
}
