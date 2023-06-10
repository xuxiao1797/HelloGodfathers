package com.example.hellogodfather.dao;

import android.content.Context;

import com.example.hellogodfather.R;
import com.example.hellogodfather.model.Like;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LikeDao {
    private static LikeDao dao;
    private LikeDao() {}
    public static LikeDao getInstance() {
        if (dao == null) {
            dao = new LikeDao();
        }
        return dao;
    }

    // FIXME: unfinished

    /**
     * Find likes according to given post_id, return all the like records to this post.
     * If no like to this post is found, then a null will be returned.
     * @param context context of android activity
     * @param postID post_id
     * @return a list of Like or null.
     */
    public List<Like> findLikesByPost(Context context, String postID) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.like_record), StandardCharsets.UTF_8))) {
            ArrayList<Like> likeList = null; // Like is stored by tuple(like_id, user_id, post_id), represent 'user_id' likes 'post_id'.
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens[2].equals(postID)) {
                    // TODO: Not yet consider the consequence of user_id does not exist in local record.
                    if (likeList == null) likeList = new ArrayList<>();
                    Like like = new Like();
                    like.setLikeId(tokens[0]);
                    like.setAuthorID(tokens[1]);
                    like.setPostID(tokens[2]);
                    likeList.add(like);
                }
            }
            return likeList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
