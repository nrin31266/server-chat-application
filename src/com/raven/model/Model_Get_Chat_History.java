
package com.raven.model;

public class Model_Get_Chat_History {
    private int senderID;
    private int receiverID;

    public Model_Get_Chat_History() {
    }

    public Model_Get_Chat_History(int senderID, int receiverID) {
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    
    
    
}
