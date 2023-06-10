package com.example.hellogodfather;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.dao.UserFirebaseDao;
import com.example.hellogodfather.model.User;
import com.example.hellogodfather.search.SearchActivity;
import com.example.hellogodfather.dao.PostDao;
import com.example.hellogodfather.model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class PostActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton writePost;
    BottomNavigationView btmNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initView();

        // Initialize firebase auth.
        FirebaseAuth firebaseAuth1 = FirebaseAuth.getInstance();
        // check current user state
        if (firebaseAuth1.getCurrentUser() == null) {
            // If current user is not logged in, restart the login activity.
            // If the login state is overdue, restart the login activity.
            startLoginActivity();
        } else {
            FirebaseUser firebaseUser = firebaseAuth1.getCurrentUser();
        }

        // Get current user information by userID
        PostFirebaseDao postDatabase = PostFirebaseDao.getInstance();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        // Retrieve current user Instance by userID and load the recyclerView of the posts
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(uid)) {
                        User userInfo = dataSnapshot.getValue(User.class);
                        postDatabase.loadDataForRecyclerView(PostActivity.this, recyclerView, userInfo);

                        writePost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startSendPostActivity(userInfo);
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        // Listener for the bottom navigation
        btmNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_home:
                        Intent intent = new Intent(PostActivity.this, PostActivity.class);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_search:
                        Intent intentSearching = new Intent(PostActivity.this, SearchActivity.class);
                        startActivity(intentSearching);
                        return true;
                    case R.id.navigation_setting:
                        Intent intentSetting = new Intent(PostActivity.this,Setting.class);
                        startActivity(intentSetting);
                        return true;
                }
                return false;
            }
        });


    }

    /**
     * Initiate the view components for PostActivity
     */
    private void initView() {
        btmNavView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        writePost = (FloatingActionButton) findViewById(R.id.btn_write_post);
        recyclerView = (RecyclerView) findViewById(R.id.post_RecyclerView);

    }

    /**
     * Switch to LoginActivity
     */

    private void startLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish(); // end this activity, call on the onDestroy() method.
    }

    /**
     * Switch to SendPostActivity
     * @param user The user information Pass to the SendPostActivity
     */
    private void startSendPostActivity(User user) {
        Intent toWritePost = new Intent(PostActivity.this, SendPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("USER", user);
        toWritePost.putExtras(bundle);
        startActivity(toWritePost);
    }

    private ArrayList<Post> getPostList(String authorName) {
        MyApp application = (MyApp) getApplication();
        ArrayList<Object> postList = application.getPostTree().get(authorName);
        ArrayList<Post> posts = new ArrayList<>();
        for (Object post: postList) {
            posts.add((Post) post);
        }
        return posts;
    }
}