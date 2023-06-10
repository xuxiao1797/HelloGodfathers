package com.example.hellogodfather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellogodfather.dao.UserFirebaseDao;
import com.example.hellogodfather.datastructure.RBTreeWithList;
import com.example.hellogodfather.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    // View component
    private TextView usernameInputView;
    private EditText passwordInputView;
    private Button loginButton;
    private TextView signinClickView;
    private FirebaseAuth firebaseAuth;
    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("localhost")
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
            .setAndroidPackageName("com.example.android", true, "12")
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // remove title bar
        setContentView(R.layout.activity_login);
        // initialize view component.
        usernameInputView = findViewById(R.id.username_email_input);
        passwordInputView = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signinClickView = findViewById(R.id.SignInHereText);

        // initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            // If the current user has already logged in. Switch to PostActivity.
            String userID = firebaseAuth.getCurrentUser().getUid();
            String userEmail = firebaseAuth.getCurrentUser().getEmail();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        if (dataSnapshot.getKey().equals(userID)) {
                            User user = dataSnapshot.getValue(User.class);
                            startPostActivity(user); // start postActivity.
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("Error", "Database connection error.");
                }
            });
            // startPostActivity(userID, userEmail);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInputView.getText().toString(); // get username from view
                String password = passwordInputView.getText().toString(); // get password from view
                /*
                LoginCtrl loginctrl = new LoginCtrl();
                boolean authOK = loginctrl.userAuthentication(getApplicationContext(), username, password); // authentication.
                if (authOK) {
                    Intent intent = new Intent(LoginActivity.this, PostActivity.class);

                    // Peng Zhao
                    Bundle b = new Bundle();
                    b.putString("username", username);
                    intent.putExtras(b);

                    startActivity(intent);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Username and Password do not match!.", Toast.LENGTH_LONG);
                    toast.show();
                }

                 */
                signIn(username, password);
            }
        });
        signinClickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in Success
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    if (!user.isEmailVerified()) {
                        usernameInputView.setError("Please verify your email.");
                        // Toast.makeText(getApplicationContext(), "verification email is sent.", Toast.LENGTH_SHORT).show();
                        user.sendEmailVerification();
                        passwordInputView.setText("");
                    } else {
                        UserFirebaseDao userFirebaseDao = UserFirebaseDao.getInstance();
                        String userID = user.getUid();
                        String userEmail = user.getEmail();
                        User user_ = userFirebaseDao.retrieveUserInformation(userID);
                        // UserFirebaseDao dao = UserFirebaseDao.getInstance();
                        // dao.userRegist(getApplicationContext(), new User(userID, userEmail, password));
                        Toast.makeText(getApplicationContext(), "Log in Successed.", Toast.LENGTH_SHORT).show();
                        /*
                        Intent intentToPost = new Intent(getApplicationContext(), PostActivity.class);
                        intentToPost.putExtra("USER_ID", userID); // TODO: Considering user session.
                        //Xiao Xu
                        intentToPost.putExtra("username",userEmail);
                        startActivity(intentToPost);

                         */
//                        User userInfo = new User(userID,userEmail,password);
                        startPostActivity(user_);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Log in failed.", Toast.LENGTH_SHORT).show();
                    usernameInputView.setText("");
                    passwordInputView.setText("");
                }
            }
        });
    }

    private void startPostActivity(String userID, String userEmail) {
        Intent intentToPost = new Intent(getApplicationContext(), PostActivity.class);
        intentToPost.putExtra("USER_ID", userID);
        intentToPost.putExtra("username",userEmail);
        startActivity(intentToPost);
        finish();
    }

    private void startPostActivity(User user) {
        // FIXME:
        // Bundle userInfo = new Bundle();
        // userInfo.putSerializable("User Info", user);
        Intent intentToPost = new Intent(getApplicationContext(), PostActivity.class);
        intentToPost.putExtra("User Info", user);
        // intentToPost.putExtras(userInfo);
        startActivity(intentToPost);
        finish();
    }

}
