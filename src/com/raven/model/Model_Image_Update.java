
package com.raven.model;


public class Model_Image_Update {
    private int userID;
    private String imageData;

    public Model_Image_Update() {
    }

    public Model_Image_Update(int userID, String imageData) {
        this.userID = userID;
        this.imageData = imageData;
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
    
    
}
