package com.example.instagramclone.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.PostsAdapter;
import com.example.instagramclone.ui.login.Fragments.ProfileFragment;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {
    private TextView tvUsername;
    private TextView tvDescription;
    private ImageView ivPostImage;
    private TextView tvLowerUsername;
    private ImageView ivProfileImage;
    private TextView tvTimeStamp;
    public static final String TAG = "DetailsActivity";
    ParseFile profileImage;
    Post post;
    final FragmentManager fragmentManager = getSupportFragmentManager();


    public void setViews() {
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivPostImage = findViewById(R.id.ivPostImage);
        ivProfileImage = findViewById(R.id.ivProfilePhoto);
        tvTimeStamp = findViewById(R.id.tvTimeStamp);
        tvLowerUsername = findViewById(R.id.tvLowerUsername);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setViews();
        inflateViews();

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


}