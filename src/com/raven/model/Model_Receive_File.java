
package com.raven.model;

public class Model_Receive_File {
    private int fileID;
    private String fileName;
    private String fileExtension;
    private String fileSize;

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

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Model_Receive_File() {
    }

    public Model_Receive_File(int fileID, String fileName, String fileExtension, String fileSize) {
        this.fileID = fileID;
        this.fileName = fileName;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
    }

    
   
    
    
}

