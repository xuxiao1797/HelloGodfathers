package com.example.hellogodfather;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellogodfather.dao.UserFirebaseDao;
import com.example.hellogodfather.model.Post;
import com.example.hellogodfather.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsFragment extends AppCompatActivity {
    private Button bt_settingFragment_back;
    private Button bt_settingFragment_save;
    private EditText et_edit_nickname;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_settings_fragment);
        // Bundle b = getIntent().getExtras();

        initView();
        initListener();

    }

    private void initListener() {
        bt_settingFragment_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //back to setting activity
                Intent intent = new Intent(SettingsFragment.this,Setting.class);
                startActivity(intent);
                finish();
            }
        });
        bt_settingFragment_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the text
                String nickname = et_edit_nickname.getText().toString();

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                //save the new nickname to the database
                if(nickname.length() >=1) {
                    User user = new User(firebaseUser.getUid(), nickname, "111111");
                    UserFirebaseDao.getInstance().userInformationUpdate(getApplicationContext(), user);
                }else {
                    Toast.makeText(SettingsFragment.this, "NickName cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        bt_settingFragment_back = findViewById(R.id.bt_settingFragment_back);
        bt_settingFragment_save = findViewById(R.id.bt_settingFragment_save);
        et_edit_nickname = findViewById(R.id.et_edit_nickname);

    }
}