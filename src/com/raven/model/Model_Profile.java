package com.raven.model;

import java.sql.Date;

public class Model_Profile {

    private int userID;
    private String userName;
    private String gender;
    private String image;
    private String imageString;
    private boolean status;
    private String name;
    private String phoneNumber;
    private String date;
    private String email;
    private String coverArt;
    private String address;

    public Model_Profile() {
    }

    public Model_Profile(int userID, String userName, String gender, String image, String imageString, boolean status, String name, String phoneNumber, String date, String email, String coverArt, String address) {
        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
        this.image = image;
        this.imageString = imageString;
        this.status = status;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.email = email;
        this.coverArt = coverArt;
        this.address = address;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCoverArt() {
        return coverArt;
    }

    public void setCoverArt(String coverArt) {
        this.coverArt = coverArt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Model_Profile{" + "userID=" + userID + ", userName=" + userName + ", gender=" + gender + ", image=" + image + ", imageString=" + imageString + ", status=" + status + ", name=" + name + ", phoneNumber=" + phoneNumber + ", date=" + date + ", email=" + email + ", coverArt=" + coverArt + ", address=" + address + '}';
    }

    

}
