package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.Message;

public class MessageDeletedForEveryoneEvent {
    private Message deletedMsg;

    public MessageDeletedForEveryoneEvent(Message deletedMsg) {
        this.deletedMsg = deletedMsg;
    }

    public Message getDeletedMsg() {
        return deletedMsg;
    }

    public void setDeletedMsg(Message deletedMsg) {
        this.deletedMsg = deletedMsg;
    }
}
