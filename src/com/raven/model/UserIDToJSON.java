
package com.raven.model;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class UserIDToJSON {
    private int userID;

    public UserIDToJSON() {
    }

    public UserIDToJSON(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
