package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Like;
import com.revature.RevConnect.repositories.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public void addLike(Like l) {
        likeRepository.save(l);
    }

    public void deleteLike(Like l) {
        likeRepository.delete(l);
    }

    public List<Like> findByPostIDAndUserID(int postID, int userID) {
        return likeRepository.findByPostIDAndUserID(postID, userID);
    }

}
