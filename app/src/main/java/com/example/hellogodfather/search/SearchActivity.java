package com.example.hellogodfather.search;

import static android.os.SystemClock.sleep;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hellogodfather.PostActivity;
import com.example.hellogodfather.R;
import com.example.hellogodfather.Setting;
import com.example.hellogodfather.dao.PostFirebaseDao;
import com.example.hellogodfather.model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/*
    @author: Peng Zhao
 */
public class SearchActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 pager2;
    SearchPagerAdapter adapter;
    SearchView searchView;
    BottomNavigationView btmNavView;
    ImageView avatar;
    String profileName;
    File tmpFile;
    FirebaseAuth firebaseAuth;
    FloatingActionButton b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        b = findViewById(R.id.write_post);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BufferedReader bufferedReader;
                PostFirebaseDao postDatabase = PostFirebaseDao.getInstance();

                try {
                    bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open("1.csv"), StandardCharsets.UTF_8));
                    String line;
                    int i = 0;
                    String[] userId = new String[]{"jgFB0JhGd3Oaw9ZRUzdEuXfrrEP2", "Ui3hWwlQu4ToVnq1KTVodlXyz1L2","PRkVVernG8WFbaDq7PRrgzSm8Tg1", "K0XGFZASg3PPyeujyiBzDqf0hzm2"};
                    String[] userName = new String[] {"paul", "William", "xx123","asou"};
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] tokens = line.split(",");
                        String postContent = tokens[1];
                        String currentTime = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH)).format(new Date());
                        ArrayList<String> likeList = new ArrayList<>();
                        String profileName = userId[i] + "_profile";

                        Post newPost = postDatabase.constructPost(userId[i], userName[i], postContent,"",
                                profileName,currentTime,likeList );
                        newPost.setTags(newPost.tagsFromContent(newPost.getPostContent()));
                        postDatabase.writePost(newPost);
                        i++;
                        if (i > 3) {
                            i = 0;
                        }
                        sleep(5000L);
                    }
                    bufferedReader.close();
                }  catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        avatar = findViewById(R.id.avatar);
        profileName = firebaseAuth.getUid() + "_profile";
        try {
            tmpFile = File.createTempFile("img", "jpg");
            StorageReference reference = FirebaseStorage.getInstance().getReference(profileName);

            reference.getFile(tmpFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap image = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
                    avatar.setImageBitmap(image);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // hide top action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        tabLayout = findViewById(R.id.tabs);
        pager2 = findViewById(R.id.view_pager2);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new SearchPagerAdapter(fm, getLifecycle());
        pager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("Trends"));
        tabLayout.addTab(tabLayout.newTab().setText("News"));
        tabLayout.addTab(tabLayout.newTab().setText("Sports"));

        /*
        Switch to different tab fragments through view pager
         */
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        /*
        Switch activity through bottom navigation bar
         */
        btmNavView = findViewById(R.id.bottom_navigation);
        btmNavView.setOnItemSelectedListener(item -> {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    // TODO: switch activity
                    Intent intent = new Intent(SearchActivity.this, PostActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_search:
                    // TODO: switch activity
                    Intent intentSearching = new Intent(SearchActivity.this, SearchActivity.class);
                    startActivity(intentSearching);
                    return true;
                case R.id.navigation_setting:
                    // TODO: switch activity1
                    Intent intentSetting = new Intent(SearchActivity.this, Setting.class);
                    startActivity(intentSetting);
                    return true;
            }
            return false;
        });

        searchView = findViewById(R.id.search_view);

        searchView.setOnSearchClickListener(v -> {

        });

        searchView.setOnCloseListener(() -> false);

        Fragment f = new SearchFragment();

        /*
        Switch to search fragment
         */
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager2.setAdapter(null);
                tabLayout.setVisibility(View.GONE);
                fm.beginTransaction().replace(R.id.fragmentContainerView2, f).commit();
            }
        });

        /*
        After closing search fragment
         */
        searchView.setOnCloseListener(() -> {
            pager2.setAdapter(adapter);
            tabLayout.setVisibility(View.VISIBLE);
            fm.beginTransaction().remove(f).commit();
            return false;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}