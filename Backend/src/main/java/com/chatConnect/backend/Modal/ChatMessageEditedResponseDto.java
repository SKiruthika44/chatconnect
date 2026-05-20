package com.chatConnect.backend.Modal;

public class ChatMessageEditedResponseDto {
    private Long id;
    private String content;

    public ChatMessageEditedResponseDto(Long msgId, String content) {
        this.id = msgId;
        this.content = content;
    }

    public Long getMsgId() {
        return id;
    }

    public void setMsgId(Long msgId) {
        this.id = msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
