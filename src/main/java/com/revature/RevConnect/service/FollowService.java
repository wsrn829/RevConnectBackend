package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Follow;
import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.FollowRepository;
import com.revature.RevConnect.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public boolean isFollowing(int followerUserID, int followingUserID) {
        return followRepository.existsByFollower_UserIDAndFollowing_UserID(followerUserID, followingUserID);
    }

    public void addFollow(int followerID, int followingID) {
        // Implement the logic to add a follow
        User follower = new User(); // Create or retrieve the User entity
        User following = new User(); // Create or retrieve the User entity
        follower.setUserID(followerID);
        following.setUserID(followingID);
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(int followerID, int followingID) {
        try {
            followRepository.deleteByFollowerIdAndFollowingId(followerID, followingID);
            // Additional logging
            System.out.println("Unfollow operation successful");
        } catch (Exception e) {
            System.err.println("Error during unfollow operation: " + e.getMessage());
            throw e;
        }
    }

    // Method to find follows where the user is a follower
    public List<Follow> findByFollower(int userID) {
        return followRepository.findByFollowerUserID(userID);
    }

    public List<Follow> findByFollowing(int followingUserID) {
        return followRepository.findByFollowingUserID(followingUserID);
    }
}
