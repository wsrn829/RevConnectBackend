package com.revature.RevConnect.service;

import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.UserRepository;
import com.revature.RevConnect.repositories.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public User addUser(User u) {
        return userRepository.save(u);
    }

    public void deleteUser(User u) {
        userRepository.delete(u);
    }

    public User updateUser(User updatedUser) {
        User existingUser = userRepository.findByUserID(updatedUser.getUserID());
        if (existingUser != null) {
            // Update the existing user details
            existingUser.setUsername(updatedUser.getUsername());
            existingUser.setFirstname(updatedUser.getFirstname());
            existingUser.setLastname(updatedUser.getLastname());
            existingUser.setBio(updatedUser.getBio());
            // Note: Do not update the password if it's not provided or is null
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                existingUser.setPassword(updatedUser.getPassword());
            }
            return userRepository.save(existingUser);
        } else {
            return null; // User not found
        }
    }

    public List<User> searchUsers(String query) {
        return userRepository.findByUsernameContainingIgnoreCase(query);
    }

    public User getUser(int userID) {
        return userRepository.findByUserID(userID);
    }

    public User getUser(String username) {
        return userRepository.findByUsername(username);
    }
}
