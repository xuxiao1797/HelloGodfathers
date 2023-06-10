package com.example.hellogodfather.dao;

import android.content.Context;

import com.example.hellogodfather.R;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.Profile;
import com.example.hellogodfather.model.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PostDao {

    private static PostDao dao;
    private PostDao() {}
    public static PostDao getInstance() {
        if (dao == null) {
            dao = new PostDao();
        }
        return dao;
    }

    private User findUserInfoByUserID(Context context, String userID) {
        UserDao userDao = UserDao.getInstance();
        return userDao.retrieveUser(context, userID);
    }

    private Profile findProfileInfoByUserID(Context context, String userID) {
        ProfileDao profileDao = ProfileDao.getInstance();
        return profileDao.retrieveUserProfile(context, userID);
    }

    /**
     * Given author id, find all the posts posted by this author. If this author exists and he/she has
     * posts records, the return a list of those posts.
     * Otherwise, a null will be returned.
     * @param context context from android activity
     * @param authorID author id
     * @return A list of post or null.
     */

    public List<Post> retrievePostByAuthor(Context context, String authorID) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.post_record), StandardCharsets.UTF_8))) {
            ArrayList<Post> postList = null;
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(","); // store with 4-tuple(post_id, author_id, content, tags, )
                if (authorID.equals(tokens[1])) {
                    // If author matches, create a post instance.
                    User author = findUserInfoByUserID(context, tokens[1]);
                    Profile profile  = findProfileInfoByUserID(context, tokens[1]);
                    if (author != null) {
                        if (postList == null) postList = new ArrayList<>();
                        ArrayList<String> whoLikesThePost = new ArrayList<>(); // Some changes here: a list of id who like the post
                        for(int i=5; i< tokens.length;i++) {
                            whoLikesThePost.add(tokens[i]);
                        }
                        postList.add(new Post(tokens[0], tokens[1], author.getUsername(), tokens[2], tokens[3], tokens[4], profile.getIconImagePath(), whoLikesThePost));
                    }
                }
            }
            return postList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find all posts containing this tag. If the post's author is not exist in uses' record, then
     * this post is considered an invalid record, and thus will not be added to the postList.
     * If no such post exist in the record or an error occur when visiting the record file, a null
     * will be returned.
     * @param context context from android activity
     * @param tag provided tag
     * @return a list of Post of null.
     */
    public List<Post> retrievePostByTag(Context context, String tag) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.post_record), StandardCharsets.UTF_8))) {
            String line;
            ArrayList<Post> postList = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String tagsStr = tokens[3];
                if (!tagsStr.equals("None")) {
                    String[] tags = tagsStr.split("#");
                    for (String t : tags) {
                        if (t.equals(tag)) {
                            String authorID = tokens[1];
                            User author = findUserInfoByUserID(context, authorID);
                            Profile profile  = findProfileInfoByUserID(context, tokens[1]);
                            if (author != null) {
                                if (postList == null) postList = new ArrayList<>();
                                ArrayList<String> whoLikesThePost = new ArrayList<>();
                                for(int i=5; i< tokens.length;i++) {
                                    whoLikesThePost.add(tokens[i]);
                                }
                                postList.add(new Post(tokens[0], tokens[1], author.getUsername(), tokens[2], tokens[3], tokens[4], profile.getIconImagePath(), whoLikesThePost));
                            }
                            break;
                        }
                    }
                }
            }
            return postList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * Given list of tags, retrieve posts from local records. If there is no post containing these tags,
//     * return null.
//     * @param context context from android activity
//     * @param tags tags provided
//     * @return a list of post or null
//     */
//    public List<Post> retrievePost(Context context, String[] tags) {
//        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.post_record), StandardCharsets.UTF_8))) {
//            ArrayList<Post> postList = null;
//            HashSet<String> alreadyAddedPostID = null;
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] tokens = line.split(",");
//                String tagsStr = tokens[3];
//                if (!tagsStr.equals("None")) {
//                    // Have tags in this post.
//                    String[] tagsOfThisRecord = tagsStr.split("#");
//                    for (String tag : tags) {
//                        for (String tagInRecord : tagsOfThisRecord) {
//                            if (tag.equals(tagInRecord)) {
//                                // find matches record by tags
//                                String authorID = tokens[1];
//                                User author = findUserInfoByUserID(context, authorID);
//                                if (author != null) {
//                                    if (postList == null) postList = new ArrayList<>();
//                                    if (alreadyAddedPostID == null) alreadyAddedPostID = new HashSet<>();
//                                    // If the author still exists, add the post record to the postList.
//                                    if (!alreadyAddedPostID.contains(tokens[0])) {
//                                        // If this post have not been added to the postList, add to postList.
//                                        postList.add(new Post(tokens[0], tokens[1], author.getUsername(), tokens[2]));
//                                        alreadyAddedPostID.add(tokens[0]);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            return postList;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
    public ArrayList<Post> retrieveAllPosts(Context context) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(R.raw.post_record), StandardCharsets.UTF_8))) {
            String line;
            ArrayList<Post> postList = null;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String authorID = tokens[1];
                User author = findUserInfoByUserID(context, authorID);
                Profile profile = findProfileInfoByUserID(context, authorID);
                if (author != null) {
                    if (postList == null) postList = new ArrayList<>();
                    ArrayList<String> whoLikesThePost = new ArrayList<>();
                    for(int i=5; i< tokens.length;i++) {
                        whoLikesThePost.add(tokens[i]);
                    }
                    postList.add(new Post(tokens[0], tokens[1], author.getUsername(), tokens[2], tokens[3], tokens[4], profile.getIconImagePath(), whoLikesThePost));
                }
            }
            return postList;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
