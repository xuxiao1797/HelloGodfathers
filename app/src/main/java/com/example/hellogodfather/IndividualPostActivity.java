package com.example.hellogodfather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class IndividualPostActivity extends AppCompatActivity {
    private boolean haveLiked;
    private boolean haveReported;
    private ImageButton btn_like;
    private ImageButton btn_report;
    private ImageButton btn_back;
    private ImageButton btn_retweet;
    private TextView txt_content;
    private TextView txt_time;
    private TextView txt_userName;
    private ImageButton btn_userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_post);
        Intent retrieveInfo = getIntent();
        Bundle postInfo = retrieveInfo.getExtras();
        Post post = (Post) postInfo.getSerializable("Individual Post");
        User user = (User) postInfo.getSerializable("Current User");

        PostFirebaseDao postDatabase = PostFirebaseDao.getInstance();
        initView();
        setView(post, user.getUid());


        // Switch back to the PostActivity
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!post.getUsersLikeThePost().contains(user.getUid())) {
                    btn_like.setImageResource(R.drawable.ic_baseline_favorite_24);
                    postDatabase.likePost(IndividualPostActivity.this, post,user.getUid());

                } else {
                    Toast.makeText(IndividualPostActivity.this,"You have already thumbed the post!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTime = getDatetime();
                String postContent = post.getPostContent();
                ArrayList<String> likeList = new ArrayList<>();
                String iconPath = user.getUid()+"_profile";

                PostFirebaseDao postDatabase = PostFirebaseDao.getInstance();
                Post newPost = postDatabase.constructPost(user.getUid(), user.getUsername(), postContent,"",
                        iconPath,currentTime,likeList );
                newPost.setTags(newPost.tagsFromContent(newPost.getPostContent()));
                postDatabase.writePost(newPost);
                Toast.makeText(IndividualPostActivity.this, "You successfully repost "+post.getUserName()+"'s post!", Toast.LENGTH_SHORT).show();
            }
        });










    }
    private void initView() {
        btn_like = (ImageButton) findViewById(R.id.ind_like_post);
        btn_report = (ImageButton) findViewById(R.id.ind_report);
        btn_back   = (ImageButton) findViewById(R.id.ind_back);
        btn_retweet = (ImageButton) findViewById(R.id.ind_retweet);
        btn_userIcon = (ImageButton) findViewById(R.id.btn_userIcon);
        txt_content = (TextView) findViewById(R.id.ind_post_content);
        txt_time = (TextView) findViewById(R.id.ind_post_time);
        txt_userName = (TextView) findViewById(R.id.ind_userName);

    }

    private void setView(Post p, String userId) {
        switchImageButton(p.getUsersLikeThePost(), userId, R.drawable.ic_baseline_favorite_24, R.drawable.ic_baseline_favorite_border_24, btn_like);
        btn_retweet.setImageResource(R.drawable.ic_baseline_reply_24);
        btn_back.setImageResource(R.drawable.ic_baseline_arrow_back_24);
        btn_report.setImageResource(R.drawable.ic_baseline_report_24);
        txt_time.setText(p.getPostTime());
        txt_content.setText(p.getPostContent());
        txt_userName.setText(p.getUserName());
        String profileName = p.getAuthorID()+"_profile";
        try {
            File tmpFile = File.createTempFile("img", "jpg");
            StorageReference reference = FirebaseStorage.getInstance().getReference(profileName);
            reference.getFile(tmpFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
                    btn_userIcon.setImageBitmap(image);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void switchImageButton(ArrayList<String> idList, String userId, int imageId1, int imageId2, ImageButton button) {
        if(idList.contains(userId))
            button.setImageResource(imageId1);
        else
            button.setImageResource(imageId2);

    }

    /**
     * Method that get the current time from user
     * @return A Formatted String of date and time
     */
    private String getDatetime() {
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        return sp.format(new Date());

    }




}