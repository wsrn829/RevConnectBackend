package com.revature.RevConnect.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.RevConnect.models.*;
import com.revature.RevConnect.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ControllerREST {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @Autowired
    MessageService messageService;

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if (user.getPassword().length() >= 4 && !user.getUsername().isEmpty()) {
            if (userService.getUser(user.getUsername()) == null) {
                User result = userService.addUser(user);
                try {
                    String jsonStr = ow.writeValueAsString(result);
                    return ResponseEntity.status(200).body(jsonStr);
                }
                catch (JsonProcessingException e) {
                    return ResponseEntity.status(500).body("Internal Server Error");
                }

            }
            else return ResponseEntity.status(409).body("Username Taken");
        } else return ResponseEntity.status(400).body("Invalid Username or Password");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if (userService.getUser(user.getUsername()) != null) {
            User result = userService.getUser(user.getUsername());
            if (result.getPassword().equals(user.getPassword())) {
                try {
                    String jsonStr = ow.writeValueAsString(result);
                    return ResponseEntity.status(200).body(jsonStr);
                } catch (JsonProcessingException e) {
                    return ResponseEntity.status(500).body("Internal Server Error");
                }
            }
            else return ResponseEntity.status(409).body("Password was incorrect.");
        }
        else return ResponseEntity.status(409).body("Username not found.");
    }

    @PutMapping("/like/{postID}/{userID}")
    public ResponseEntity<String> addLike(@PathVariable int postID, @PathVariable int userID) { // TODO: Refactor with cookie
        if (likeService.findByPostIDAndUserID(postID, userID).isEmpty()) {
            Like like = new Like(postID, userID);
            likeService.addLike(like);
            return ResponseEntity.status(200).body("Like added.");
        }
        return ResponseEntity.status(400).body("You have already liked this post.");
    }

    @DeleteMapping("/like/{postID}/{userID}")
    public ResponseEntity<String> deleteLike(@PathVariable int postID, @PathVariable int userID) { // TODO: Refactor with cookie
        if (!likeService.findByPostIDAndUserID(postID, userID).isEmpty()) {
            Like like = new Like(postID, userID);
            likeService.deleteLike(like);
            return ResponseEntity.status(200).body("Like deleted.");
        }
        return ResponseEntity.status(404).body("Like not found.");
    }

    @PutMapping("/comment/{postID}")
    public ResponseEntity<String> addComment(@PathVariable int postID) {
        Comment comment = new Comment(postID);
        commentService.addComment(comment);
        return ResponseEntity.status(200).body("Comment added.");
    }

    @DeleteMapping("/comment/{postID}/{commentID}")
    public ResponseEntity<String> deleteComment(@PathVariable int postID, @PathVariable int commentID) {
        if (!commentService.getCommentsByPostAndUser(postID, commentID).isEmpty()) {
            return ResponseEntity.status(200).body("Comment deleted.");
        }
        return ResponseEntity.status(404).body("Comment not found.");
    }

    @PostMapping("/follow")
    public ResponseEntity<String> addFollow(@RequestBody Follow follow) {
        User follower = userService.getUser(follow.getFollower().getUserID());
        User following = userService.getUser(follow.getFollowing().getUserID());

        if (follower != null && following != null) {
            followService.addFollow(follower.getUserID(), following.getUserID());
            return ResponseEntity.status(200).body("Successfully followed.");
        }
        return ResponseEntity.status(404).body("User not found.");
    }

    @GetMapping("/follows/follower/{followerID}")
    public ResponseEntity<List<Follow>> getFollowsByFollowerID(@PathVariable int followerID) {
        List<Follow> follows = followService.findByFollower(followerID);
        return ResponseEntity.status(200).body(follows);
    }

    @GetMapping("/follows/following/{followingID}")
    public ResponseEntity<List<Follow>> getFollowsByFollowingID(@PathVariable int followingID) {
        List<Follow> follows = followService.findByFollowing(followingID);
        return ResponseEntity.status(200).body(follows);
    }

    @GetMapping("/follows/check")
    public ResponseEntity<Boolean> isFollowerIDFollowingFollowingID(@RequestParam int followerID, @RequestParam int followingID) {
        boolean isFollowing = followService.existsByFollowerAndFollowing(followerID, followingID);
        return ResponseEntity.status(200).body(isFollowing);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollow(@RequestParam int followerID, @RequestParam int followingID) {
        followService.unfollow(followerID, followingID);
        return ResponseEntity.status(200).body("Successfully unfollowed.");
    }

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageService.addMessage(message);
        return ResponseEntity.status(200).body("Message sent.");
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.findAllMessages();
        return ResponseEntity.status(200).body(messages);
    }

    @GetMapping("/messages/sender/{senderID}")
    public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable int senderID) {
        User sender = userService.getUser(senderID);
        if (sender != null) {
            List<Message> messages = messageService.findBySender(sender);
            return ResponseEntity.status(200).body(messages);
        }
        return ResponseEntity.status(404).body(null);
    }

    @DeleteMapping("/message/{messageID}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageID) {
        if (messageService.existsById(messageID)) {
            messageService.deleteMessage(messageID);
            return ResponseEntity.status(200).body("Message deleted.");
        }
        return ResponseEntity.status(404).body("Message not found.");
    }

    @GetMapping("/messages/{senderID}/{receiverID}")
    public ResponseEntity<List<Message>> getMessagesBetweenUsers(@PathVariable int senderID, @PathVariable int receiverID) {
        User sender = userService.getUser(senderID);
        User receiver = userService.getUser(receiverID);
        if (sender != null && receiver != null) {
            List<Message> messages = messageService.findMessagesBetweenUsers(sender, receiver);
            return ResponseEntity.status(200).body(messages);
        }
        return ResponseEntity.status(404).body(null);
    }

}
