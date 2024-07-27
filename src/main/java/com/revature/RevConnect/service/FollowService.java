package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Follow;
import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.FollowRepository;
import com.revature.RevConnect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new follow relationship
    public void addFollow(int followerID, int followingID) {
        User follower = userRepository.findByUserID(followerID);
        User following = userRepository.findByUserID(followingID);

        if (follower != null && following != null) {
            Follow follow = new Follow(follower, following);
            followRepository.save(follow);
        }
    }

    // Find all follows by follower ID
    public List<Follow> findByFollower(int followerID) {
        User follower = userRepository.findByUserID(followerID);
        return follower != null ? followRepository.findByFollower(follower) : Collections.emptyList();
    }

    // Find all follows by following ID
    public List<Follow> findByFollowing(int followingID) {
        User following = userRepository.findByUserID(followingID);
        return following != null ? followRepository.findByFollowing(following) : Collections.emptyList();
    }

    // Check if a user is following another user
    public boolean existsByFollowerAndFollowing(int followerID, int followingID) {
        User follower = userRepository.findByUserID(followerID);
        User following = userRepository.findByUserID(followingID);
        return follower != null && following != null && followRepository.existsByFollowerAndFollowing(follower, following);
    }

    // Unfollow a user
    public void unfollow(int followerID, int followingID) {
        User follower = userRepository.findByUserID(followerID);
        User following = userRepository.findByUserID(followingID);
        if (follower != null && following != null) {
            followRepository.deleteByFollowerAndFollowing(follower, following);
        }
    }
}
