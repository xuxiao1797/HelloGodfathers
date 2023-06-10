package com.example.hellogodfather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hellogodfather.dao.UserFirebaseDao;
import com.example.hellogodfather.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView emailInputView;
    private TextView passwordInputView;
    private TextView passwordConfirmInputView;
    private Button signInButton;


    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("localhost")
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
            .setAndroidPackageName("com.example.android", true, "12")
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // Initialize firebase authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize view component
        emailInputView = findViewById(R.id.SignInEmailInput);
        passwordInputView = findViewById(R.id.SIgnInPasswordInput);
        passwordConfirmInputView = findViewById(R.id.SignInPasswordConfirmInput);
        signInButton = findViewById(R.id.SignInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInputView.getText().toString();
                String password = passwordInputView.getText().toString();
                String confirm = passwordConfirmInputView.getText().toString();
                if (!checkPasswordMatchWithConfirmPassword(password, confirm)) {
                    passwordInputView.setError("Password does not match with confirmation.");
                    passwordInputView.setText("");
                    passwordConfirmInputView.setText("");
                    // Toast.makeText(getApplicationContext(), "Password does not match with confirmation!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkEmailFormat(email)) {
                    emailInputView.setText("");
                    emailInputView.setError("Email's format is incorrect!");
                    // Toast.makeText(getApplicationContext(), "Email format incorrect!", Toast.LENGTH_SHORT).show();
                    passwordInputView.setText("");
                    passwordConfirmInputView.setText("");
                    return;
                }
                // signIn(email, password);
                createUser(email, password);
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
                    String userID = user.getUid();
                    String userEmail = user.getEmail();
                    // UserFirebaseDao dao = UserFirebaseDao.getInstance();
                    // dao.userRegist(getApplicationContext(), new User(userID, userEmail, password));
                    Toast.makeText(getApplicationContext(), "Log in Successed.", Toast.LENGTH_SHORT).show();
                    Intent intentToPost = new Intent(getApplicationContext(), PostActivity.class);
                    intentToPost.putExtra("USER_ID", userID); // TODO: Considering user session.
                    startActivity(intentToPost);
                    // TODO: Update UI.
                } else {
                    Toast.makeText(getApplicationContext(), "Log in failed.", Toast.LENGTH_SHORT).show();
                    emailInputView.setText("");
                    passwordInputView.setText("");
                    // TODO: Update UI.
                }
            }
        });
    }

    private void createUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Verify email.
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();
                            UserFirebaseDao dao = UserFirebaseDao.getInstance();
                            dao.userRegist(getApplicationContext(), new User(user.getUid(), email, password));
                            // Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getApplicationContext(), "Create new user success!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // TODO: Do sth
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Create new user failed!", Toast.LENGTH_SHORT).show();
                    emailInputView.setText("");
                    passwordInputView.setText("");
                }
            }
        });
    }

    /*
    (task) -> {
            if (task.isSuccessful()) {
                // send verification email
                FirebaseUser user = firebaseAuth.getCurrentUser();
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Verification email has been sent", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: Do sth
                    }
                });
                Toast.makeText(getApplicationContext(), "User Created", Toast.LENGTH_SHORT).show();
                String userID = user.getUid();
            }
        }
     */

    private boolean checkPasswordMatchWithConfirmPassword(String password, String confirm) {
        return password.equals(confirm);
    }
    private boolean checkEmailFormat(String email) {
        // TODO: check email format.
        return true;
    }
}