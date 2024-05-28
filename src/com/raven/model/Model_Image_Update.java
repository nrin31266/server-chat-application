package com.raven.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Model_Image_Update {
    private int userID;
    private String imageData;
    private boolean lastChunk; // Thêm thuộc tính này

    public Model_Image_Update() {
    }

    public Model_Image_Update(int userID, String imageData, boolean lastChunk) {
        this.userID = userID;
        this.imageData = imageData;
        this.lastChunk = lastChunk;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getImageData() {
        return imageData;
    }

    public void setImageData(String imageData) {
        this.imageData = imageData;
    }

    public boolean isLastChunk() {
        return lastChunk;
    }

    public void setLastChunk(boolean lastChunk) {
        this.lastChunk = lastChunk;
    }
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        try {
            json.put("userID", userID);
            json.put("imageData", imageData);
            json.put("lastChunk", lastChunk);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Model_Image_Update{" + "userID=" + userID + ", lastChunk=" + lastChunk + ", imageData=" + imageData + '}';
    }
    
}
