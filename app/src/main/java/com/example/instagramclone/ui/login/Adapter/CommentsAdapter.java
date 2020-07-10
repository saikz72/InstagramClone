package com.example.instagramclone.ui.login.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Model.Comment;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    List<Comment> comments;
    Context context;

    public CommentsAdapter(List<Comment> comments, Context context){
        this.comments = comments;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        final CommentsAdapter.ViewHolder holder = new CommentsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private TextView tvTimeStamp;
        private TextView tvComment;
        private ImageView ivProfilePhoto;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivProfilePhoto = itemView.findViewById(R.id.ivProfilePhoto);
        }

        public void bind(Comment comment) {
            try {
                tvUsername.setText(comment.getUser().fetchIfNeeded().getUsername());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvTimeStamp.setText(comment.getRelativeTimeAgo(comment.getDate().toString()));
            tvComment.setText(comment.getCommentText());
            ParseFile profile = comment.getUser().getParseFile(Post.KEY_PROFILE_IMAGE);
            if(profile != null){
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfilePhoto);
            }
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        comments.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Comment> list) {
        comments.addAll(list);
        notifyDataSetChanged();
    }
}
