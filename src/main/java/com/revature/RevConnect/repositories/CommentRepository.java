package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByPostID(int postID);
    List<Comment> findByPostIDAndCommentID(int postID, int commentID);

}
