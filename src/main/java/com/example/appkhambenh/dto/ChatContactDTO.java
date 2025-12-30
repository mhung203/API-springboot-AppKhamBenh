package com.example.appkhambenh.dto;

import java.time.LocalDateTime;

public class ChatContactDTO {
    private Integer userId;
    private String fullName;
    private String avatar;
    private String lastMessage;
    private LocalDateTime time;
    public ChatContactDTO(Integer userId, String fullName, String avatar, String lastMessage, LocalDateTime time) {
        this.userId = userId;
        this.fullName = fullName;
        this.avatar = avatar;
        this.lastMessage = lastMessage;
        this.time = time;
    }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}