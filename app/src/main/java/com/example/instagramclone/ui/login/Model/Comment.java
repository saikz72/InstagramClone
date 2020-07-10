package com.example.instagramclone.ui.login.Model;

import android.text.format.DateUtils;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_COMMENT_TEXT = "commentText";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_POST_ID = "postID";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }
    public String getPostID(){
        return getString(KEY_POST_ID);
    }
    // Sets the String id of the Post that this Comment is for.
    public void setPostId(Post postId) {
        put(KEY_POST_ID, postId);
    }
    public Date getDate() {
        return this.getCreatedAt();
    }

    public String getCommentText(){
        return getString(KEY_COMMENT_TEXT);
    }
    public void setCommentText(String commentText){
        put(KEY_COMMENT_TEXT, commentText);
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }
}
