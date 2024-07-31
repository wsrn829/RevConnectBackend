package com.revature.RevConnect.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Follows")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "USER_ID")
    @JsonBackReference("follower")
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FOLLOWING_ID", referencedColumnName = "USER_ID")
    @JsonBackReference("following")
    private User following;

    @Transient
    private String followerUsername;

    @Transient
    private String followingUsername;

    @Transient
    private String followerBio;

    @Transient
    private String followingBio;

    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
        this.followerUsername = follower.getUsername();
        this.followingUsername = following.getUsername();
        this.followerBio = follower.getBio();
        this.followingBio = following.getBio();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
        this.followerUsername = follower.getUsername();
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
        this.followingUsername = following.getUsername();
    }

    public String getFollowerUsername() {
        return followerUsername;
    }

    public String getFollowingUsername() {
        return followingUsername;
    }

    public String getFollowerBio() {
        return followerBio;
    }

    public void setFollowerBio(String followerBio) {
        this.followerBio = followerBio;
    }

    public String getFollowingBio() {
        return followingBio;
    }

    public void setFollowingBio(String followingBio) {
        this.followingBio = followingBio;
    }
}
