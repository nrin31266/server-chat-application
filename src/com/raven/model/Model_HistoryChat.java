package com.raven.model;

import java.time.LocalDateTime;

public class Model_HistoryChat {
    
    private int fromUser;
    private int toUser;
    private int type;
    private String txt;
    private String senderFilePath;
    private String receiverFilePath;
    private LocalDateTime time;  // Nếu vẫn muốn giữ thời gian
    
    public Model_HistoryChat() {
    }

    public Model_HistoryChat(int fromUser, int toUser, int type, String txt, String senderFilePath, String receiverFilePath) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.type = type;
        this.txt = txt;
        this.senderFilePath = senderFilePath;
        this.receiverFilePath = receiverFilePath;
    }

    public String getSenderFilePath() {
        return senderFilePath;
    }

    public void setSenderFilePath(String senderFilePath) {
        this.senderFilePath = senderFilePath;
    }

    public String getReceiverFilePath() {
        return receiverFilePath;
    }

    public void setReceiverFilePath(String receiverFilePath) {
        this.receiverFilePath = receiverFilePath;
    }

    

    public int getFromUser() {
        return fromUser;
    }

    public void setFromUser(int fromUser) {
        this.fromUser = fromUser;
    }

    public int getToUser() {
        return toUser;
    }

    public void setToUser(int toUser) {
        this.toUser = toUser;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }


    



   
    
}