package com.example.hellogodfather.dao;

import android.content.Context;

import com.example.hellogodfather.R;
import com.example.hellogodfather.model.Profile;
import com.example.hellogodfather.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ProfileDao {
    private static ProfileDao dao;
    private ProfileDao() {}
    public static ProfileDao getInstance() {
        if (dao == null) {
            dao = new ProfileDao();
        }
        return dao;
    }

    /**
     * Find user's profile by provided user_id. If this user's record exists, then a profile
     * instance will be returned.
     * Otherwise, a null will be returned.
     * @param context context of android activity.
     * @param userID user_id
     * @return A Profile instance or null.
     */
    public Profile retrieveUserProfile(Context context, String userID) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.profile_record), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(","); // store in format(user_id, image_filepath)
                if (tokens[0].equals(userID)) {
                    UserDao userDao = UserDao.getInstance();
                    String username = userDao.retrieveUsernameByUserID(context, tokens[0]);
                    if (username == null) {
                        return null; // this user's information does not exist in local record. Return null.
                    }
                    Profile profile = new Profile();
                    profile.setUserID(tokens[0]);
                    profile.setUserName(username);
                    profile.setIconImagePath(tokens[1]);
                    return profile;
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
