package com.example.hellogodfather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SendPostActivity extends AppCompatActivity {
    private EditText content;
    private ImageButton sendPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);
        initView();


        Intent retrieveInfo = getIntent();
        Bundle b = retrieveInfo.getExtras();
        User user = (User) b.getSerializable("USER");
        String username = user.getUsername();
        String userId = user.getUid();

        sendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentTime = getDatetime();
                String postContent = content.getText().toString();


                if(postContent.isEmpty()) {
                    Toast.makeText(SendPostActivity.this, "Post upload failed: Nothing is written", Toast.LENGTH_SHORT).show();
                } else {
                    // Retrieve user's Icon image
                    ArrayList<String> likeList = new ArrayList<>();
                    String profileName = user.getUid() + "_profile";
                    PostFirebaseDao postDatabase = PostFirebaseDao.getInstance();
                    Post newPost = postDatabase.constructPost(userId, username, postContent, "",
                            profileName, currentTime, likeList);
                    newPost.setTags(newPost.tagsFromContent(newPost.getPostContent()));
                    postDatabase.writePost(newPost);

                    Toast.makeText(SendPostActivity.this, "Send Successful", Toast.LENGTH_SHORT).show();
                }
                toPostActivity();
                finish();
            }
        });

    }





    /**
     * Method that get the current time from user
     * @return A Formatted String of date and time
     */
    private String getDatetime() {
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        return sp.format(new Date());

    }

    /**
     * Initialize view components
     */

    private void initView() {
        content = (EditText) findViewById(R.id.write_post_content);
        sendPost = (ImageButton) findViewById(R.id.btn_send_post);
    }

    /**
     * Switch to PostActivity
     */
    private void toPostActivity() {
        Intent toPostPage = new Intent(SendPostActivity.this, PostActivity.class);
        startActivity(toPostPage);

    }






}