package com.example.instagramclone.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import android.view.MenuItem;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Fragments.ComposeFragment;
import com.example.instagramclone.ui.login.Fragments.PostsFragment;
import com.example.instagramclone.ui.login.Fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private BottomNavigationView bottomNavigationView;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    public static final int RADIUS_DP = 30;
    public static final int MARGIN_DP = 10;

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
                        break;
                    case R.id.action_home:
                        //TODO: update fragment
                        fragment  = new PostsFragment();
                        break;
                    case R.id.action_profile:
                        //TODO: update fragment
                        fragment  = new ProfileFragment();
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