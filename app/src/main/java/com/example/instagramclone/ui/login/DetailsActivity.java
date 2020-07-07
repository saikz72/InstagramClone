package com.example.instagramclone.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.PostsAdapter;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivPostImage;
    public static final String TAG = "DetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivPostImage = findViewById(R.id.ivPostImage);

        Post post = Parcels.unwrap(getIntent().getParcelableExtra(PostsAdapter.POST_DETAILS));
        tvUsername.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        ParseFile image = post.getImage();
        if (image != null)
            Glide.with(this).load(image.getUrl()).into(ivPostImage);
    }

}