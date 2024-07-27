package com.revature.RevConnect.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Users")
public class User {

    //User info

    //Primary key for identification
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    int userID;

    //Display info
    @Column(name="USERNAME")
    String username;

    @Column(name="FIRSTNAME")
    String firstname;

    @Column(name="LASTNAME")
    String lastname;

    @Column(name="BIO")
    String bio;

    //authentication TODO: encrypt
    @Column(name="PASSWORD")
    String password;

    // Relationship with Follow entity (followers)
    @OneToMany(mappedBy = "following")
    @JsonManagedReference("following")
    private List<Follow> followers;

    // Relationship with Follow entity (followings)
    @OneToMany(mappedBy = "follower")
    @JsonManagedReference("follower")
    private List<Follow> followings;

    public User() {};

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Follow> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follow> followers) {
        this.followers = followers;
    }

    public List<Follow> getFollowings() {
        return followings;
    }

    public void setFollowings(List<Follow> followings) {
        this.followings = followings;
    }
}
