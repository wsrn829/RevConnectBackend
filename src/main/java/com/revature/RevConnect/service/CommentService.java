package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Comment;
import com.revature.RevConnect.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public void addComment(Comment c) {
        commentRepository.save(c);
    }

    public void deleteComment(Comment c) {
        commentRepository.delete(c);
    }

    //TODO: update
    public void updateComment(Comment c) {

    }

    public List<Comment> getCommentsByPost(int postID) {
        return commentRepository.findByPostID(postID);
    }

    public List<Comment> getCommentsByPostAndUser(int postID, int commentID) {
        return commentRepository.findByPostIDAndCommentID(postID, commentID);
    }

}
