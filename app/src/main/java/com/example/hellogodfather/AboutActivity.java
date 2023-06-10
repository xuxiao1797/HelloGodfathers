package com.example.hellogodfather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellogodfather.model.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {
    private TextView tv_numOfLikes;
    private TextView tv_numOfPosts;
    private Button bt_settingFragment_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
    }

    private void initListener() {
        bt_settingFragment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to setting activity
                Intent intent = new Intent(AboutActivity.this, Setting.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        String userId = FirebaseAuth.getInstance().getUid();
        tv_numOfLikes = findViewById(R.id.tv_numOfLikes);
        tv_numOfPosts = findViewById(R.id.tv_numOfPosts);
        bt_settingFragment_back = findViewById(R.id.bt_about_back);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Post");

        //get all the post of the user from the database

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Post> postList = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    if (key != null) {
                        Post post = ds.getValue(Post.class);
                        assert post != null;
                        String postUsername = post.getAuthorID();
                        if (postUsername != null && userId != null) {
                            if (postUsername.equals(userId)) {
                                postList.add(post);
                            }
                        }
                    }
                }

                String numOfPosts = String.valueOf(postList.size());
                tv_numOfPosts.setText(numOfPosts);

                int numOfLikes = -2*postList.size();
                for(Post p : postList){
                    numOfLikes += p.countLikes();
                }
                tv_numOfLikes.setText(String.valueOf(numOfLikes));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






    }



}