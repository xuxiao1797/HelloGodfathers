package com.example.hellogodfather.dao;

import android.content.Context;

import com.example.hellogodfather.R;
import com.example.hellogodfather.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class UserDao {
    private static UserDao dao = null;
    private UserDao() {}
    public static UserDao getInstance() {
        if (dao == null) {
            dao = new UserDao();
        }
        return dao;
    }

    /**
     * check login information from local records. If username and password of 'user' matches the
     * local record, set the userID and return the user instance.
     * If the given username does not match the password or no such username in the record, a null
     * instance will be returned.
     * @param context context from android activity.
     * @param user an instance of User, containing username and password, with userID unset.
     * @return an instance of User or null.
     */
    public User userLoginCheck(Context context, User user) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.user_info), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(","); // store as tuple(id, username, password)
                if (user.getUsername().equals(tokens[1]) && user.getPassword().equals(tokens[2])) {
                    // check username and password.
                    user.setUid(tokens[0]);
                    return user;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public User userLoginCheck(Context context, String username, String password) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.user_info), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(","); // store as tuple(id, username, password)
                if (username.equals(tokens[1]) && password.equals(tokens[2])) {
                    // check username and password.
                    User user = new User(tokens[0], tokens[1], tokens[2]);
                    return user;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retrieve User's information by given user id. If this user id exists in record, return an
     * instance of this user.
     * Otherwise, a null will be returned.
     * @param context context from android activity
     * @param userID provided user id
     * @return an instance of user or null
     */
    public User retrieveUser(Context context, String userID) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.user_info), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (userID.equals(tokens[0])) {
                    return new User(tokens[0], tokens[1], tokens[2]);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String retrieveUsernameByUserID(Context context, String userID) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.user_info), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (userID.equals(tokens[0])) {
                    return tokens[1];
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
