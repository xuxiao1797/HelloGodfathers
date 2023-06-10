package com.example.hellogodfather;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.datastructure.RBTreeWithList;
import com.example.hellogodfather.model.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyApp extends Application {
    private RBTreeWithList<String> postTree = new RBTreeWithList<>();

    public RBTreeWithList<String> getPostTree() {
        return this.postTree;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference postRef = database.getReference("Post");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {
                    if (ds.hasChild("userName")) {
                        Log.d("Application.onCreate", "Insert to rbTree");
                        String username = ds.child("userName").getValue(String.class);
                        Post post = ds.getValue(Post.class);
                        ArrayList<Object> postList = new ArrayList<>();
                        postList.add(post);
                        postTree.put(username, postList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Application.onCreate", "database error");
            }
        };
        postRef.addListenerForSingleValueEvent(valueEventListener);
    }

}
