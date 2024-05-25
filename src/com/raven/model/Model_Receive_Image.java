package com.raven.model;

public class Model_Receive_Image {

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
    

    public Model_Receive_Image(int fileID, String image, int width, int height,String fileExtension,String fileName){
        this.fileID = fileID;
        this.image = image;
        this.width = width;
        this.height = height;
        this.fileExtension=fileExtension;
        this.fileName=fileName;
    }

    public Model_Receive_Image() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    private int fileID;
    private String image;
    private int width;
    private int height;
    private String fileExtension;
    private String fileName;
}
