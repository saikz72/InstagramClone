package com.example.instagramclone.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.CommentsAdapter;
import com.example.instagramclone.ui.login.Adapter.PostsAdapter;
import com.example.instagramclone.ui.login.Fragments.ProfileFragment;
import com.example.instagramclone.ui.login.Model.Comment;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivPostImage;
    private TextView tvLowerUsername;
    private ImageView ivProfileImage;
    private TextView tvTimeStamp;
    private ImageView ivComment;
    private EditText etComment;
    private SwipeRefreshLayout swipeContainer;
    public static final String TAG = "DetailsActivity";
    ParseFile profileImage;
    Post post;
    RecyclerView rvComments;
    CommentsAdapter commentsAdapter;
    List<Comment> allComments;


    public void setViews() {
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivPostImage = findViewById(R.id.ivPostImage);
        ivProfileImage = findViewById(R.id.ivProfilePhoto);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        tvLowerUsername = findViewById(R.id.tvLowerUsername);
        rvComments = findViewById(R.id.rvComments);
        ivComment = findViewById(R.id.ivComment);
        etComment = findViewById(R.id.etComment);
        swipeContainer = findViewById(R.id.swipeContainer);
    }

    public void inflateViews() {
        post = Parcels.unwrap(getIntent().getParcelableExtra(PostsAdapter.POST_DETAILS));
        String text = post.getUser().getUsername();
        tvUsername.setText(text);
        tvLowerUsername.setText(text);
        tvDescription.setText(post.getDescription());
        String timeAgo = post.getRelativeTimeAgo(post.getDate().toString());
        tvTimeStamp.setText(timeAgo);
        ParseFile image = post.getImage();
        if (image != null)
            Glide.with(this).load(image.getUrl()).into(ivPostImage);
        profileImage = post.getUser().getParseFile(Post.KEY_PROFILE_IMAGE);
        if (profileImage != null)
            Glide.with(this).load(profileImage.getUrl()).transform(new CircleCrop()).into(ivProfileImage);


    }

    //comment listener
    public void onCommentSent(){
        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment();
                comment.setCommentText(etComment.getText().toString());
                comment.setUser(ParseUser.getCurrentUser());
                comment.setPostId(post);
                comment.saveInBackground();
                etComment.setText("");
                queryComment();

            }
        });
    }

    //profile image listerner
    public void onProfilePhotoTap(){
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseFile image = ParseUser.getCurrentUser().getParseFile(Post.KEY_PROFILE_IMAGE);

                Log.d(TAG, "onClick: "+ "  image==> " + image + "   profile ===> " +profileImage);
                if (!image.equals(profileImage)) {
                    Intent intent = new Intent(DetailsActivity.this, ProfileDetailsActivity.class);
                    intent.putExtra("userProfile", post.getUser());
                    startActivity(intent);
                }
            }
        });

    }

    //listerner for refresh
    public void refreshPage(){
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryComment();
            }
        });
    }
    public void queryComment(){
        final ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
        query.addDescendingOrder(Comment.KEY_CREATED_AT);
        query.whereEqualTo(Comment.KEY_POST_ID, post);
        query.findInBackground(new FindCallback<Comment>() {
            @Override
            public void done(List<Comment> comments, ParseException e) {
                if(e != null){
                    Toast.makeText(DetailsActivity.this, "Issue with getting comments", Toast.LENGTH_SHORT).show();
                    return;
                }
                commentsAdapter.clear();
                commentsAdapter.addAll(comments);
                swipeContainer.setRefreshing(false);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setViews();
        refreshPage();

        allComments = new ArrayList<>();
        commentsAdapter = new CommentsAdapter(allComments, this);
        //set the adapter to the rv
        rvComments.setAdapter(commentsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvComments.setLayoutManager(linearLayoutManager);
        inflateViews();
        queryComment();
        onProfilePhotoTap();
        onCommentSent();

    }


}