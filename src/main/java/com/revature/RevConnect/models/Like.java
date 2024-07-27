package com.revature.RevConnect.models;

import jakarta.persistence.*;

@Entity
@Table(name="Likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LIKE_ID")
    int likeID;

    //PostID
    @Column(name="POST_ID")
    int postID;

    //userID
    @Column(name="USER_ID")
    int userID;

    public Like() {}

    //Constructor
    public Like (int postID, int userID) {
        this.postID = postID;
        this.userID = userID;
    }

    public int getLikeID() {
        return likeID;
    }

    public void setLikeID(int likeID) {
        this.likeID = likeID;
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
