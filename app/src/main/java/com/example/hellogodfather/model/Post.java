package com.example.hellogodfather.model;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post implements Serializable{
    /** @Author Yucheng Zhao
     * @Description The model of a post
     * postID ： ID of the post
     * authorID : ID of author who writes the post (Need to get from User model)
     * userName: Nickname of the author
     * postContent: content of what author wrote
     * postTime: time of the post
     * userIconPath: Image Path of users' Icon (Need to get from Profile Model)
     * usersLikesThePost: An arrayList of userID collects who like the post
     * */
    private String postID;
    private String authorID;
    private String userName;
    private String tags;
    private String postContent;
    private String postTime;
    private String userIconPath;
//    private String likes;
    private ArrayList<String> usersLikeThePost = new ArrayList<>();


    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    // From Firebase Document
    public Post() {

    }




    public Post(String postID, String authorID, String userName, String postContent,
                String tags, String postTime, String userIconPath, ArrayList<String> usersLikeThePost) {
        this.postID = postID;
        this.authorID = authorID;
        this.userName = userName;
        this.tags     = tags;
        this.postContent = postContent;
        this.postTime    = postTime;
        this.userIconPath = userIconPath;
        usersLikeThePost.add("#");
        this.usersLikeThePost = usersLikeThePost;
    }

    public String getPostID() {
        return postID;
    }

    public String getAuthorID() {
        return authorID;
    }

    public ArrayList<String> getUsersLikeThePost() {
        return usersLikeThePost;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getUserIconPath() { return userIconPath; }

    public String getTags() { return tags; }


    public void setPostID(String postID) {
        this.postID = postID;
    }

    public void setAuthorID(String authorID) {
        this.authorID = authorID;
    }

    public void setUsersLikeThePost(ArrayList<String> usersLikeThePost) {
        if(this.usersLikeThePost.size() == 0) usersLikeThePost.add("#");
        this.usersLikeThePost = usersLikeThePost;
    }


    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setTags(String tags) { this.tags = tags; }

    public void setUserIconPath(String userIconPath) { this.userIconPath = userIconPath; }

    @NonNull
    @Override
    public String toString() {
        return "Post{" +
                "postID='" + postID + '\'' +
                ", authorID='" + authorID + '\'' +
                ", userName='" + userName + '\'' +
                ", tags='" + tags + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postTime='" + postTime + '\'' +
                ", userIconPath='" + userIconPath + '\'' +
                ", usersLikeThePost=" + usersLikeThePost +
                '}';
    }

    /**
     * Method to read the post content and convert multiple string of tags as a String
     * Sample: ""
     * @param postContent that reads from the content
     * @return A string of tags
     */
    public String tagsFromContent(String postContent){
        Pattern pattern_pound = Pattern.compile("(#[a-zA-Z0-9_]+)");
        Matcher matcher_pound = pattern_pound.matcher(postContent);
        StringBuilder matchedTags = new StringBuilder();
        while (matcher_pound.find()) { matchedTags.append(matcher_pound.group()); }
        return matchedTags.toString().toLowerCase();
    }

    public int countLikes() {
        return usersLikeThePost.size();
    }

//    @Exclude
//    public Map<String, Object> toMap() {
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("author_id", authorID);
//        result.put("author_name", userName);
//        result.put("content", postContent);
//        result.put("postTime", postTime);
//        result.put("who_likes_the_post", usersLikeThePost);
//        return result;
//    }
}
