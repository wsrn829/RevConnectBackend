package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Follow;
import com.revature.RevConnect.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {

    List<Follow> findByFollowerUserID(int followerUserID);

    List<Follow> findByFollowingUserID(int followingUserID);

    // Check if a user is following another user
    boolean existsByFollower_UserIDAndFollowing_UserID(int followerUserID, int followingUserID);

    // Unfollow a user
    void deleteByFollower_UserIDAndFollowing_UserID(int followerUserID, int followingUserID);

    @Modifying
    @Query("DELETE FROM Follow f WHERE f.follower.id = :followerID AND f.following.id = :followingID")
    void deleteByFollowerIdAndFollowingId(@Param("followerID") int followerID, @Param("followingID") int followingID);}
