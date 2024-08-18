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
        User follower = userRepository.findById(followerID).orElseThrow(() -> new RuntimeException("Follower not found"));
        User following = userRepository.findById(followingID).orElseThrow(() -> new RuntimeException("Following not found"));
        Follow follow = new Follow(follower, following);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(int followerID, int followingID) {
        if (isFollowing(followerID, followingID)) {
            try {
                followRepository.deleteByFollower_UserIDAndFollowing_UserID(followerID, followingID);
                System.out.println("Unfollow operation successful");
            } catch (Exception e) {
                System.err.println("Error during unfollow operation: " + e.getMessage());
                throw e;
            }
        } else {
            System.out.println("Unfollow operation failed: Not following");
        }
    }

    public List<Follow> findByFollower(int userID) {
        return followRepository.findByFollowerUserID(userID);
    }

    public List<Follow> findByFollowing(int followingUserID) {
        return followRepository.findByFollowingUserID(followingUserID);
    }
}