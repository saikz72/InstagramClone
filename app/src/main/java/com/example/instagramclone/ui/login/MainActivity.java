package com.example.instagramclone.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Fragments.ComposeFragment;
import com.example.instagramclone.ui.login.Fragments.PostsFragment;
import com.example.instagramclone.ui.login.Model.Post;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        //queryPosts();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_compose:
                        fragment  = new ComposeFragment();
                        Toast.makeText(MainActivity.this, "There is no image!!!", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.action_home:
                        //TODO: update fragment
                        Toast.makeText(MainActivity.this, "There is ddfd image!!!", Toast.LENGTH_LONG).show();
                        fragment  = new PostsFragment();
                        break;
                    case R.id.action_profile:
                        //TODO: update fragment
                        Toast.makeText(MainActivity.this, "There is no jfk!!!", Toast.LENGTH_LONG).show();
                        fragment  = new ComposeFragment();
                        break;
                    default:
                        fragment  = new ComposeFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

   }