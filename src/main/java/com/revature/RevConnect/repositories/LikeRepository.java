package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like,Integer> {
    List<Like> findByPostIDAndUserID(int postID, int userID);
}
