package com.example.instagramclone.ui.login.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;
import com.example.instagramclone.ui.login.Adapter.PostsAdapter;
import com.example.instagramclone.ui.login.Model.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class PostsFragment extends Fragment {
    private RecyclerView rvPosts;
    public static final String TAG = "PostsFragment";
    private PostsAdapter postsAdapter;
    private List<Post> allPosts;
    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        postsAdapter = new PostsAdapter(getContext(), allPosts);

        //set the adapter to the rv
        rvPosts.setAdapter(postsAdapter);

        //set the layout manager on the recycler view
        rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        queryPosts();
    }
    private void queryPosts(){
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    Log.e(TAG, "Issue with getting posts",e);
                    return;
                }
                for (Post post : posts){
                    Log.i(TAG, "done: "+ post.getDescription());

                allPosts.add(post);
                postsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}