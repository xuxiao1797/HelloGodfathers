package com.example.hellogodfather.dao;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hellogodfather.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UserFirebaseDao {
    private static final String TAG = UserFirebaseDao.class.getSimpleName();
    private static UserFirebaseDao dao;
    private DatabaseReference database;
    private User user = new User();
    private UserFirebaseDao() {
        database = FirebaseDatabase.getInstance().getReference().child("users");
    }
    public static UserFirebaseDao getInstance() {
        if (dao == null) {
            dao = new UserFirebaseDao();
        }
        return dao;
    }

    /**
     * User Regist. Given user's information, store it to firebase realtime database.
     * TODO: Find out a way to give each user a unique id.
     * @param context context of android activity
     * @param user user instance.
     */
    public void userRegist(Context context, User user) {
        // FIXME: Find a way to give each user an unique id. Maybe Authentication.
        // String key = database.child("users").push().getKey();
        String key = user.getUid();
        Map<String, Object> userAuthValue = user.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(key, userAuthValue);
        // TODO: Considering also adding a record on profile.
        database.updateChildren(childUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast toast = Toast.makeText(context, "User regist suceeded.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(context, "User Regist failed.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void userInformationUpdate(Context context, User user) {
        // String key = user.getUserID();
        String key = user.getUid();
        Map<String, Object> userInfo = user.toMap();
        Map<String, Object> childUpdate = new HashMap<>();
        childUpdate.put(key, userInfo);
        database.updateChildren(childUpdate).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast toast = Toast.makeText(context, "User Info update suceeded.", Toast.LENGTH_SHORT);
                toast.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast toast = Toast.makeText(context, "User Info Update failed.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
    public User retrieveUserInformation(Context context, String userID, TextView textView) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(userID)) {
                        user = dataSnapshot.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return user;
    }

    public User retrieveUserInformation(String userID) {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(userID)) {
                        user = dataSnapshot.getValue(User.class);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return user;
    }
}
