package com.example.hellogodfather.dao;


import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellogodfather.MyApp;
import com.example.hellogodfather.PostAdaptor;
import com.example.hellogodfather.datastructure.RBTreeWithList;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.example.hellogodfather.tok_parser.Parser;
import com.example.hellogodfather.tok_parser.Query;
import com.example.hellogodfather.tok_parser.Tokenizer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostFirebaseDao {
    private static PostFirebaseDao dao;
    private final DatabaseReference database;
    public Post p = new Post();



    private PostFirebaseDao() {
        database = FirebaseDatabase.getInstance().getReference("Post"); // initialize database reference
    }

    public static PostFirebaseDao getInstance() {
        if (dao == null) {
            dao = new PostFirebaseDao();
        }
        return dao;
    }




    /**
     * Write a new post into database
     * Using the key provided by firebase as
     * the postID
     * @param post The Post instance
     * @Author Yucheng Zhao
     */
    public void writePost(Post post) {
        String postId = database.push().getKey();
        post.setPostID(postId);
        assert postId != null;
        database.child(postId).setValue(post);
    }

    /**
     * Retrieve a Post by its postId
     * @param postId writer's id
     * @return The Post found in database
     */
    public Post retrievePost(String postId) {

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    if (key != null) {
                        if (key.equals(postId)) {
                            p = ds.getValue(Post.class);
                            break;
                        }
                    }
                }
                Log.d(TAG, "value is" + p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed");
            }
        });
        return p;
    }



    /**
     * Load all posts  from firebase onto the recyclerView(Post)
     *
     * @param context Context of the activity
     * @param view    RecyclerView for visualize posts
     * @param user  The current user
     */
    public void loadDataForRecyclerView(Context context, RecyclerView view, User user) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> postList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    if (key != null) {
                        Post post = ds.getValue(Post.class);
                        postList.add(post);
                    }
                }
                Collections.reverse(postList);
                PostAdaptor postAdaptor = new PostAdaptor(context, postList, user);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                view.setLayoutManager(linearLayoutManager);
                view.setAdapter(postAdaptor);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed");
            }
        });

    }

    /**
     * Get posts by given query and update UI
     * @param context context context of android activity
     * @param recyclerView recyclerView recyclerView for visualize posts
     * @param textView textView textView for query result
     * @param query query
     * @param user current user
     * @param myApp storing posts by usernames in RBTree
     */
    public void loadDataForRecyclerViewByQuery(Context context, RecyclerView recyclerView, TextView textView, String query, User user, MyApp myApp) {
        // tokenize and parse query into lists
        RBTreeWithList<String> postTree =  myApp.getPostTree();
        try {
            Tokenizer tokenizer = new Tokenizer(query);
            if (tokenizer.current() != null) {
                Parser parser = new Parser(tokenizer);
                Query q = parser.parseQuery();
                List<String> usernames = q.evaluateUsernames();
                List<String> tags = q.evaluateTags();

                // read data from firebase
                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<Post> postsByTags = new ArrayList<>();
                        ArrayList<Post> postsByUsernames = new ArrayList<>();
                        ArrayList<Post> posts = new ArrayList<>();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String key = ds.getKey();
                            if (key != null) {
                                Post post = ds.getValue(Post.class);
                                assert post != null;

                                // Get posts by given tags from firebase
                                if (tags != null) {
                                    if (post.getTags() != null) {
                                        String[] postTags = post.getTags().split("#");
                                        for (String tag : tags) {
                                            char[] t = tag.toCharArray();
                                            for (String postTag : postTags) {
                                                char[] pt = postTag.toCharArray();
                                                if (t.length <= pt.length) {
                                                    boolean match = true;
                                                    for (int i = 0; i < t.length; i++) {
                                                        if (t[i] != pt[i]) {
                                                            match = false;
                                                            break;
                                                        }
                                                    }
                                                    if (match) {
                                                        postsByTags.add(post);
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Get posts by given usernames from data stored
                        if (usernames != null) {
                            ArrayList<Object> postsTemp = new ArrayList<>();
                            for (String username : usernames){
                                postsTemp = postTree.get(username);
                            }
                            if (postsTemp != null) {
                                for (Object o : postsTemp) {
                                    postsByUsernames.add((Post)o);
                                }
                            }
                        }

                                                /*
                            Get posts that
                            1. satisfies both tags and usernames
                            2. satisfies usernames when tags is null
                            3. satisfies tags when usernames is null
                             */
                        if (usernames != null && tags != null) {
                            for (Post p : postsByUsernames) {
                                for (Post pst : postsByTags) {
                                    if (p.getPostID().equals(pst.getPostID())) {
                                        posts.add(p);
                                    }
                                }
                            }
                        }
                        if (tags == null && usernames != null) {
                            posts.addAll(postsByUsernames);
                        }
                        if (usernames == null && tags != null) {
                            posts.addAll(postsByTags);
                        }


                        // Update UI
                        if (posts.isEmpty()) {
                            textView.setText("No results for " + query);
                        } else {
                            textView.setText("");
                        }

                        Collections.reverse(posts);
                        // Load recyclerView
                        PostAdaptor postAdaptor = new PostAdaptor(context, posts, user);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(postAdaptor);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "Failed");
                    }
                });
            }
        } catch (Exception e){
            recyclerView.setAdapter(null);
            textView.setText(query + " is an invalid query.");
            Toast toast = Toast.makeText(context, "Invalid query", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    /**
     * Data storage for user- like activity
     *
     * @param context context of activity
     * @param post    Current post the user is viewing
     * @param userId  Viewer's id
     * @Author Yucheng Zhao
     */
    public void likePost(Context context, Post post, String userId) {
        ArrayList<String> usersLikeThePost = post.getUsersLikeThePost();
        if (usersLikeThePost.contains(userId)) {
            Toast.makeText(context, "You have already thumbed the post!", Toast.LENGTH_LONG).show();
        } else {
            usersLikeThePost.add(userId);
            post.setUsersLikeThePost(usersLikeThePost);
            database.child(post.getPostID()).setValue(post);
            Toast.makeText(context, "You thumbed for the post by " + post.getUserName(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * A temple constructor when storing into firebase database
     * Note: using id from firebase, so we currently do not need to initialize id.
     *
     * @param userId           writer's id
     * @param userName         writer's name
     * @param content          writing content
     * @param tags             tags extract from the content
     * @param setUserIconPath  Path of writer's icon
     * @param currentTime      Publish time
     * @param usersLikeThePost ArrayList of userId who like the post
     * @return The post constructor
     */

    public Post constructPost(String userId, String userName, String content, String tags, String setUserIconPath,
                              String currentTime, ArrayList<String> usersLikeThePost) {

        return new Post("", userId, userName, content, tags, currentTime, setUserIconPath, usersLikeThePost);
    }


//    /**
//     * Store a new published post to firebase realtime database.
//     *
//     * @param context context of android activity
//     * @param post    post instance
//     */
//    public void publishNewPost(Context context, Post post) {
//        if (post == null)
//            return;
//        String key = database.child("posts").push().getKey(); // This key is unique!
//        Map<String, Object> postValues = post.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/posts/" + key, postValues);
//        childUpdates.put("/user-posts/" + post.getAuthorID() + "/" + key, postValues);
//        database.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast toast = Toast.makeText(context, "Post Upload to Firebase Succeeded.", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast toast = Toast.makeText(context, "Post Upload to Firebase Failed.", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//    }

    //    public void postInformationUpdate(Context context, Post post) {
//        // String key = post.getUserID();
//        String key = post.getPostID();
//        Map<String, Object> postInfo = post.toMap();
//        Map<String, Object> childUpdate = new HashMap<>();
//        childUpdate.put(key, postInfo);
//        database.updateChildren(childUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Toast toast = Toast.makeText(context, "User Info update suceeded.", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast toast = Toast.makeText(context, "User Info Update failed.", Toast.LENGTH_SHORT);
//                toast.show();
//            }
//        });
//    }
}
