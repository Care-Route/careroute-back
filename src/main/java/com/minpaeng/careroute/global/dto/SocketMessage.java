package com.minpaeng.careroute.global.dto;

import lombok.Getter;

@Getter
public class SocketMessage {
    private final int to;
    private final Message message;

    public SocketMessage(int to, String title, String content) {
        this.to = to;
        this.message = new Message(title, content);
    }

    @Getter
    public static class Message {
        private final String title;
        private final String content;

        public Message(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
