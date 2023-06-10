package com.example.hellogodfather;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PostAdaptor extends RecyclerView.Adapter<PostAdaptor.ViewHolder> {
    private  Context ctx;
    private  ArrayList<Post> postList;
    private User user;

    public PostAdaptor(Context ctx, ArrayList<Post> postList, User user){
        this.ctx = ctx;
        this.postList = postList;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post model = postList.get(position);
        holder.userName.setText(model.getUserName());
        loadUserIcon(model,holder);

        holder.postContent.setText(model.getPostContent());
        holder.postTime.setText(model.getPostTime());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent to_view_individual_post_activity = new Intent(ctx, IndividualPostActivity.class);
                    Bundle postInfo = new Bundle();
                    postInfo.putSerializable("Individual Post", model);
                    postInfo.putSerializable("Current User", user);
                    to_view_individual_post_activity.putExtras(postInfo);
                    ctx.startActivity(to_view_individual_post_activity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView userIcon;
        private final TextView userName, postContent, postTime;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userIcon = (ImageView) itemView.findViewById(R.id.userIcon);
            userName = (TextView) itemView.findViewById(R.id.userName);
            postContent = (TextView) itemView.findViewById(R.id.postContent);
            postTime = (TextView) itemView.findViewById(R.id.postTime);
        }
    }

    /**
     * Load user's Icon from firebase storage
     * @param model Post model of the current post in the list
     * @param holder View holder of the RecyclerView
     */

    private void loadUserIcon(Post model, ViewHolder holder) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String profileName = model.getAuthorID()+"_profile";
        try {
            File tmpFile = File.createTempFile("img", "jpg");
            StorageReference reference = FirebaseStorage.getInstance().getReference(profileName);
            reference.getFile(tmpFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
                    holder.userIcon.setImageBitmap(image);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}



