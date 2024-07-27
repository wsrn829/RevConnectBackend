package com.revature.RevConnect.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "Follows")
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOLLOW_ID")
    private Integer followID;

    // The ID of the user who is following
    @ManyToOne
    @JoinColumn(name = "FOLLOWER_ID", referencedColumnName = "USER_ID")
    @JsonBackReference("follower")
    private User follower;

    // The ID of the user being followed
    @ManyToOne
    @JoinColumn(name = "FOLLOWING_ID", referencedColumnName = "USER_ID")
    @JsonBackReference("following")
    private User following;

    public Follow() {}

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    public Integer getFollowID() {
        return followID;
    }

    public void setFollowID(Integer followID) {
        this.followID = followID;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowing() {
        return following;
    }

    public void setFollowing(User following) {
        this.following = following;
    }
}
