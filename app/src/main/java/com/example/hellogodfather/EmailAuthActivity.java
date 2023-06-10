package com.example.hellogodfather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;

public class EmailAuthActivity extends AppCompatActivity {

    private TextView emailInput;
    private Button authButton;
    private FirebaseAuth firebaseAuth;

    private ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl("localhost")
            .setHandleCodeInApp(true)
            .setIOSBundleId("com.example.ios")
            .setAndroidPackageName("com.example.android", true, "12")
            .build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_auth);
        emailInput = findViewById(R.id.email_auth_input);
        authButton = findViewById(R.id.email_auth_button);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                firebaseAuth.sendSignInLinkToEmail(email, actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Signin Success", Toast.LENGTH_SHORT).show();
                            // TODO : start another activity.
                        } else {
                            Toast.makeText(getApplicationContext(), "Signin Failed", Toast.LENGTH_SHORT).show();
                            // TODO : Return to login activity.
                        }
                    }
                });
            }
        });
    }
}