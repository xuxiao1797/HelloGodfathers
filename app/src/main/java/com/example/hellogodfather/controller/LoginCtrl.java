package com.example.hellogodfather.controller;

import android.content.Context;

import com.example.hellogodfather.dao.UserDao;
import com.example.hellogodfather.model.User;
import com.google.firebase.auth.FirebaseAuth;


public class LoginCtrl {
    /**
     * User authentication (login)
     * @param context context of android activity
     * @param username username
     * @param password password
     * @return true if username and password matches; otherwise, return false.
     */
    public boolean userAuthentication(Context context, String username, String password) {
        UserDao userDao = UserDao.getInstance();
        User user = userDao.userLoginCheck(context, username, password);
        return user != null;
    }

}
