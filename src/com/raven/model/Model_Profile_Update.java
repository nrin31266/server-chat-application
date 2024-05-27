
package com.raven.model;

public class Model_Profile_Update {
    private int userID;
    private String userName;
    private String gender;
    private String phoneNumber;
    private String date;
    private String email;
    private String address;

    public Model_Profile_Update() {
    }

    public Model_Profile_Update(int userID, String userName, String gender, String phoneNumber, String date, String email, String address) {
        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Model_Profile_Update{" + "userID=" + userID + ", userName=" + userName + ", gender=" + gender + ", phoneNumber=" + phoneNumber + ", date=" + date + ", email=" + email + ", address=" + address + '}';
    }

    
    
    
    
    
}
