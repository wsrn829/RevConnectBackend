package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {
    Post findByPostID(int postID);
    List<Post> findByUserID(int userID);
}
