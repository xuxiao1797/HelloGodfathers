package com.example.hellogodfather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.firebase.FirebaseApp;

import java.io.DataOutput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        Button testActivity = findViewById(R.id.button);
        testActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });
    }
    /*
    "WHat the fxxk is Trump doing #Trump"
    database: [#Trump]
    query(trump) -> true
    query(tag=Bayden) -> false
    #Bayden
    S -> #(characters)|S
    characters -> character | digit | characters
    character -> [a-zA-Z] | character
    digit -> [0-9]
    ArrayList<>[tag1 tag2] tags
    PostAcivity.search(tags) -> postCtrl(DAO dao ->query(tags))

     */
}