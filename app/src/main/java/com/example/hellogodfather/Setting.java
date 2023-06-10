
package com.example.hellogodfather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hellogodfather.model.User;
import com.example.hellogodfather.search.SearchActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class Setting extends AppCompatActivity {

    private TextView tv_nickName;
    private ImageView iv_profile;
    private Button bt_takephoto;
    private Button bt_selectPhotos;
    private static Uri path;
    private Button bt_setting_logout;
    private TextView tv_setting_settings;
    private TextView tv_setting_about;
    private Bitmap new_profile;
    private TextView tv_GPS;
    private String profileName = "";
    private FirebaseAuth firebaseAuth;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://hellogodfather-d028b.appspot.com");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance(); // get the firebase Instance
        setContentView(R.layout.activity_setting);

        try {
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }
        initListener();


        //set the nickName TextView
        //if the user do not have a nickName, show the email
        tv_nickName = findViewById(R.id.tv_nickName);
        //get the uid of the current user
        String uid = firebaseAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tv_nickName.setText(firebaseAuth.getCurrentUser().getEmail());
        DatabaseReference ref = database.getReference("users");
        //Traverse the database find the username of the user
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(uid)) {
                        User user = dataSnapshot.getValue(User.class);
                        tv_nickName.setText(user.getUsername());


                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        bt_setting_logout = findViewById(R.id.bt_setting_logout);
        //set the click event of logout
        bt_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Setting.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Added by Ruizheng, call onDestroy() method to end this activity to ensure safety.
            }
        });


        //add the bottom navigation
        BottomNavigationView btmNavView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        btmNavView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_home:
                        // TODO: switch activity
                        Intent intent = new Intent(Setting.this, PostActivity.class);
                        // intent.putExtras(b);
                        startActivity(intent);
                        return true;
                    case R.id.navigation_search:
                        // TODO: switch activity
                        Intent intentSearching = new Intent(Setting.this, SearchActivity.class);
                        // intentSearching.putExtras(b);
                        startActivity(intentSearching);
                        return true;
                    case R.id.navigation_setting:
                        // TODO: switch activity1
                        Intent intentSetting = new Intent(Setting.this, Setting.class);
                        // intentSetting.putExtras(b);
                        startActivity(intentSetting);
                        return true;
                }
                return false;
            }
        });
    }

    private void initListener() {
        //set the click event of settings
        tv_setting_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this,SettingsFragment.class);
                startActivity(intent);

            }
        });
        //set the click event of profile
        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bt_takephoto.getVisibility() == View.INVISIBLE){
                    bt_takephoto.setVisibility(View.VISIBLE);
                    bt_selectPhotos.setVisibility(View.VISIBLE);
                }else {
                    bt_takephoto.setVisibility(View.INVISIBLE);
                    bt_selectPhotos.setVisibility(View.INVISIBLE);
                }
            }
        });

        //set the click event of about
        tv_setting_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        //set the click event of GPS
        tv_GPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isGoogleServiceAvailable()) {
                    Intent intent = new Intent(Setting.this, GPSActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(Setting.this, "Google Play services not supported by this device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_takephoto.setOnClickListener(this::onClick);
        bt_selectPhotos.setOnClickListener(this::onClick);
    }

    //return true if the device support google play services
    public boolean isGoogleServiceAvailable(){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }


    private void initView() throws IOException {
        tv_nickName = findViewById(R.id.tv_nickName);
        iv_profile = findViewById(R.id.iv_profile);
        bt_takephoto  = findViewById(R.id.bt_takephoto);
        bt_selectPhotos = findViewById(R.id.bt_selectPhotos);
        bt_takephoto.setVisibility(View.INVISIBLE);
        bt_selectPhotos.setVisibility(View.INVISIBLE);
        tv_GPS = findViewById(R.id.tv_GPS);


        //get the profile name according to uid
        profileName = firebaseAuth.getCurrentUser().getUid()+ "_profile";
        File tmpFile = File.createTempFile("img", "jpg");
        //get the profile of the user from firebase storage
        StorageReference reference = FirebaseStorage.getInstance().getReference(profileName);

        reference.getFile(tmpFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
                iv_profile.setImageBitmap(image);
            }
        });


        tv_setting_settings = findViewById(R.id.tv_setting_settings);
        tv_setting_about = findViewById(R.id.tv_setting_about);

    }


    public void onClick(View v) {
        if (v.getId() == R.id.bt_selectPhotos) {

            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, 1);

        }else if(v.getId() == R.id.bt_takephoto)
            try {

                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                //        "profile.jpg")));
                startActivityForResult(intent1, 2);
            } catch (Exception e) {
                Toast.makeText(Setting.this, "No camera permission", Toast.LENGTH_LONG).show();
            }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            path = data.getData();
        }else {
            return;
        }
        try {
            //get image from the album
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);

            //Setting image to profile
            iv_profile.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //select photos from album
        if(requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if(data.getData() != null) {
                    editPhoto(data.getData());
                }
                //savePic(data.getData());
            }
        }else if(requestCode == 2){
            //take photos
            if (resultCode == Activity.RESULT_OK) {
                File temp = new File(Environment.getExternalStorageDirectory()
                        + "/profile.jpg");
                editPhoto(Uri.fromFile(temp));
                //savePic(Uri.fromFile(temp));
            }
        }else {
            Bundle extras = data.getExtras();
            new_profile = extras.getParcelable("data");
            if (new_profile != null) {
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), new_profile, null,null));
                savePic(uri);

                iv_profile.setImageBitmap(new_profile);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    private void savePic(Uri uri){
        //get the uid
        String uid = firebaseAuth.getCurrentUser().getUid();
        profileName = uid + "_profile";
        StorageReference childRef = storageRef.child(profileName);
        //uploading the image
        UploadTask uploadTask = childRef.putFile(uri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Setting.this, "upload successful", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editPhoto(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,3);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // User user = UserFirebaseDao.getInstance().retrieveUserInformation(this, uid, tv_nickName);
    }
}