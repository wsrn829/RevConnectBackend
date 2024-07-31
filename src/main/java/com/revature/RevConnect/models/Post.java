package com.revature.RevConnect.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Posts")
public class Post {
    //Primary key for identification
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="POST_ID")
    int postID;

    //TODO: add image support, also expand into post header, body text, body image. For now, its just a text.
    @Column(name="POST")
    String post;

    @Column(name="USER_ID")
    int userID;

    public Post() {}

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
