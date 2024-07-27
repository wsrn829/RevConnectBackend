package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Follow;
import com.revature.RevConnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    // Find all follows by follower User
    List<Follow> findByFollower(User follower);

    // Find all follows by following User
    List<Follow> findByFollowing(User following);

    // Check if a user is following another user
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Unfollow a user
    void deleteByFollowerAndFollowing(User follower, User following);
}
