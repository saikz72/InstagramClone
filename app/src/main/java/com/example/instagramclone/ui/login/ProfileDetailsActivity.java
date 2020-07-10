package com.example.instagramclone.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.PostsAdapter;
import com.example.instagramclone.ui.login.Adapter.ProfileAdapter;
import com.example.instagramclone.ui.login.Fragments.PostsFragment;
import com.example.instagramclone.ui.login.Model.EndlessRecyclerViewScrollListener;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetailsActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    private ImageView ivProfilePhoto;
    private TextView tvUsername;
    RecyclerView rvPost;
    protected ProfileAdapter profileAdapter;
    protected List<Post> allPosts;
    ParseUser user;
    private EndlessRecyclerViewScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        setViews();

        Intent intent = getIntent();
        // CHANGE LATER
        user = intent.getParcelableExtra("userProfile");
        allPosts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, allPosts);

        //set the adapter to the rv
        rvPost.setAdapter(profileAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        //set the layout manager on the recycler view
        rvPost.setLayoutManager(gridLayoutManager);
        bindViews();

        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        rvPost.addOnScrollListener(scrollListener);
        queryPosts();
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadNextDataFromApi(PostsFragment.MAX_POST_NUMBER);
            }
        });
    }

    private void loadNextDataFromApi(int page) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.setLimit(PostsFragment.MAX_POST_NUMBER ); //limit the return post
        query.setSkip(PostsFragment.MAX_POST_NUMBER * page);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Toast.makeText(ProfileDetailsActivity.this, "Issue with getting posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                // allPosts.addAll(posts);
                //postsAdapter.notifyDataSetChanged();
                profileAdapter.addAll(posts);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    public void setViews() {
        tvUsername = findViewById(R.id.tvUsername);
        ivProfilePhoto = findViewById(R.id.ivProfilePhoto);
        rvPost = findViewById(R.id.rvPosts);
        swipeContainer = findViewById(R.id.swipeContainer);

    }

    public void bindViews() {
        tvUsername.setText(user.getUsername());
        ParseFile profileImage = user.getParseFile(Post.KEY_PROFILE_IMAGE);
        Glide.with(this).load(profileImage.getUrl()).transform(new CircleCrop()).into(ivProfilePhoto);
    }

    protected void queryPosts() {
        // Lookup the swipe container view
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.setLimit(PostsFragment.MAX_POST_NUMBER); //limit the return post
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Toast.makeText(ProfileDetailsActivity.this, "Issue with getting posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                profileAdapter.clear();
                profileAdapter.addAll(posts);
                swipeContainer.setRefreshing(false);

            }
        });
    }
}