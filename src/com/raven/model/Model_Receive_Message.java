
package com.raven.model;


public class Model_Receive_Message {
    int fromUserID;
    String text;
    private int messageType;

    public int getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(int fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Model_Receive_Message(int messageType, int fromUserID, String text) {
        this.fromUserID = fromUserID;
        this.text = text;
        this.messageType = messageType;
    }

    public Model_Receive_Message() {
    }

    @Override
    public String toString() {
        return "Model_Receive_Message{" + "fromUserID=" + fromUserID + ", text=" + text + ", messageType=" + messageType + '}';
    }
    
    
    
    
    
}
