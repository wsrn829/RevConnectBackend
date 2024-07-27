package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Post;
import com.revature.RevConnect.repositories.PostRepository;
import com.revature.RevConnect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    void addPost(Post post) {
        postRepository.save(post);
    }

    void deletePost(Post post) {
        postRepository.delete(post);
    }

    //TODO: update
    void updatePost(Post post) {

    }

    Post getPostById(int postID) {

        return postRepository.findByPostID(postID);
    }

    List<Post> getPostsByAuthor(int userID) {

        return postRepository.findByUserID(userID);
    }

    //TODO: Get post by headline (once posts have headlines)

}
