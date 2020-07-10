package com.example.instagramclone.ui.login.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.ProfileAdapter;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends PostsFragment {
    private SwipeRefreshLayout swipeContainer;
    private Button btnAddPhoto;
    private ImageView ivProfilePhoto;
    private TextView tvUsername;
    File photoFile;
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    ParseFile profilePicture;
    ProfileAdapter profileAdapter;
    RecyclerView rvPost;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        btnAddPhoto = view.findViewById(R.id.btnAddPhoto);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvUsername = view.findViewById(R.id.tvUsername);
        rvPost = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        profileAdapter = new ProfileAdapter(getContext(), allPosts);

        //set the adapter to the rv
        rvPost.setAdapter(profileAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        //set the layout manager on the recycler view
        rvPost.setLayoutManager(gridLayoutManager);


        try {
            profilePicture = ParseUser.getCurrentUser().fetch().getParseFile(("profilePhoto"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvUsername.setText(ParseUser.getCurrentUser().getUsername());

        Glide.with(getContext()).load(profilePicture.getUrl()).transform(new CircleCrop()).into(ivProfilePhoto);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                queryPosts();
            }
        });

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
                saveProfile(ParseUser.getCurrentUser(), photoFile);
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + photoFileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivProfilePhoto.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    protected void queryPosts() {
        // Lookup the swipe container view
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(MAX_POST_NUMBER); //limit the return post
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Toast.makeText(getContext(), "Issue with getting posts", Toast.LENGTH_SHORT).show();
                    return;
                }
                profileAdapter.clear();
                profileAdapter.addAll(posts);
                swipeContainer.setRefreshing(false);

            }
        });
    }

    public void saveProfile(ParseUser currentUser, File pic){
        ParseFile parsePic = new ParseFile(photoFile);
        currentUser.put("profilePhoto", parsePic);
        currentUser.saveInBackground();
    }
}
