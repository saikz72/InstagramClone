package com.example.instagramclone.ui.login.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.DetailsActivity;
import com.example.instagramclone.ui.login.Fragments.ProfileFragment;
import com.example.instagramclone.ui.login.MainActivity;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    public static final String POST_DETAILS = "postDetails";
    List<Post> posts;
    private Context context;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        // Allow users to be clickable
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentManager = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
                Fragment profileFragment = new ProfileFragment();
                fragmentManager.replace(R.id.flContainer, profileFragment);
                fragmentManager.addToBackStack(null).commit();

            }
        };
        holder.tvUsername.setOnClickListener(listener);
        holder.ivProfilePhoto.setOnClickListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of post -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvUsername;
        private TextView tvDescription;
        private TextView tvTimeStamp;
        private ImageView ivPostImage;
        private ImageView ivProfilePhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
            itemView.setOnClickListener(this);
        }

        public void bind(Post post) {
            tvUsername.setText(post.getUser().getUsername());
            tvDescription.setText(post.getDescription());
            String timeAgo = post.getRelativeTimeAgo(post.getDate().toString());
            tvTimeStamp.setText(timeAgo);
            ParseFile postImage = post.getImage();
            if (postImage != null)
                Glide.with(context).load(postImage.getUrl()).into(ivPostImage);
            ParseFile profilePhoto = post.getUser().getParseFile("profilePhoto");
            if(profilePhoto != null)
                Glide.with(context).load(profilePhoto.getUrl()).transform(new CircleCrop()).into(ivProfilePhoto);

        }

        @Override
        public void onClick(View view) {
            //gets item position
            int position = getAdapterPosition();
            //make sure the position is valid, i.e actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                Post post = posts.get(position);
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(POST_DETAILS, Parcels.wrap(post));
                context.startActivity(intent);
            }
        }

    }
}
