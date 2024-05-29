package com.raven.model;

import java.time.LocalDateTime;

public class Model_HistoryChat {
    
    private int fromUser;
    private int toUser;
    private int type;
    private String txt;
    private String senderFilePath;
    private String receiverFilePath;
    private int fileID;
    private String fileName;
    private String fileSize;
    private String time;  // Nếu vẫn muốn giữ thời gian

    public Model_HistoryChat() {
    }

    public Model_HistoryChat(int fromUser, int toUser, int type, String txt, String senderFilePath, String receiverFilePath, int fileID, String fileName, String fileSize) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.type = type;
        this.txt = txt;
        this.senderFilePath = senderFilePath;
        this.receiverFilePath = receiverFilePath;
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileSize = fileSize;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    
    
    
}