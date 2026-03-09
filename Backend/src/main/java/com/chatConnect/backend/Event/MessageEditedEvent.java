package com.chatConnect.backend.Event;

import com.chatConnect.backend.Modal.Message;

public class MessageEditedEvent {
    private Message chatMessage;

    public MessageEditedEvent(Message chatMessage) {
        this.chatMessage = chatMessage;
    }

    public Message getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(Message chatMessage) {
        this.chatMessage = chatMessage;
    }
}
