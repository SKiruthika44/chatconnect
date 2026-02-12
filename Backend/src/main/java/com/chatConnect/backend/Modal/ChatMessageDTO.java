package com.chatConnect.backend.Modal;

public class ChatMessageDTO {

    String receiver;
    String content;





    public ChatMessageDTO(String receiver, String content) {
        this.receiver = receiver;
        this.content = content;

    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Override
    public String toString() {
        return "ChatMessageDTO{" +
                "receiver='" + receiver + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
