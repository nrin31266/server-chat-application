
package com.raven.model;



public class Model_All_File {
    private int fileID;
    private String nameFile;
    private String fileExtension;
    private String status;

    public Model_All_File() {
    }

    public Model_All_File(int fileID, String nameFile, String fileExtension, String status) {
        this.fileID = fileID;
        this.fileExtension = fileExtension;
        this.status = status;
    }

    public int getFileID() {
        return fileID;
    }

    public void setFileID(int fileID) {
        this.fileID = fileID;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNameFile() {
        return nameFile;
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }
    
}

