package com.example.hellogodfather.model;

import com.example.hellogodfather.dao.ProfileDao;

public class Profile {
    private String userID;
    private String userName;
    private String iconImagePath;
    // TODO: other profile information
    public Profile() {}

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIconImagePath() {
        return iconImagePath;
    }

    public void setIconImagePath(String iconImagePath) {
        this.iconImagePath = iconImagePath;
    }
}
