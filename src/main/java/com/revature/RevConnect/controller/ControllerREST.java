package com.revature.RevConnect.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.revature.RevConnect.models.*;
import com.revature.RevConnect.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ControllerREST {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenService tokenService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @Autowired
    ChatService chatService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/register")
    @CrossOrigin(
            origins = "http://localhost:3000",
            allowedHeaders = "*",
            allowCredentials = "true"
    )

    public ResponseEntity<String> registerUser(@RequestBody User user, HttpServletResponse response) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if (user.getPassword().length() >= 4 && !user.getUsername().isEmpty()) {
            if (userService.getUser(user.getUsername()) == null) {
                User result = userService.addUser(user);

                Map<String, String> claimsMap = new HashMap<>();
                claimsMap.put("username", result.getUsername());
                claimsMap.put("userID", String.valueOf(result.getUserID()));

                Cookie cookie = new Cookie("Authentication", this.tokenService.generateToken(claimsMap));
                response.addCookie(cookie);

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
    @CrossOrigin
    public ResponseEntity<String> loginUser(@RequestBody User user, HttpServletResponse response) {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        if (userService.getUser(user.getUsername()) != null) {
            User result = userService.getUser(user.getUsername());
            if (result.getPassword().equals(user.getPassword())) {

                Map<String, String> claimsMap = new HashMap<>();
                claimsMap.put("username", result.getUsername());
                claimsMap.put("userID", String.valueOf(result.getUserID()));

                Cookie cookie = new Cookie("Authentication", this.tokenService.generateToken(claimsMap));
                response.addCookie(cookie);

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

    @GetMapping("/users/{userID}")
    public ResponseEntity<User> getUser(@PathVariable int userID) {
        User user = userService.getUser(userID);
        if (user != null) {
            return ResponseEntity.status(200).body(user);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/users/{userID}")
    public ResponseEntity<String> updateUser(@CookieValue("Authentication") String bearerToken, @PathVariable int userID, @RequestBody User user) {
        Integer loggedInUserID = tokenService.returnAuthID(bearerToken);
        if (loggedInUserID == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        if (!loggedInUserID.equals(userID)) {
            return ResponseEntity.status(403).body("Forbidden: Cannot update another user's profile");
        }

        // Add validation to ensure user object is valid
        if (user.getUsername() == null || user.getFirstname() == null || user.getLastname() == null || user.getBio() == null) {
            return ResponseEntity.status(400).body("Invalid user data");
        }

        User updatedUser = userService.updateUser(user);
        if (updatedUser != null) {
            try {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String jsonStr = ow.writeValueAsString(updatedUser);
                return ResponseEntity.status(200).body(jsonStr);
            } catch (JsonProcessingException e) {
                return ResponseEntity.status(500).body("Internal Server Error");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.status(200).body(users);
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
    public ResponseEntity<String> follow(@CookieValue("Authentication") String bearerToken, @RequestParam int followingID) {
        Integer loggedInUserID = tokenService.returnAuthID(bearerToken);
        if (loggedInUserID == null) {
            return ResponseEntity.status(403).body("Unauthorized to follow.");
        }

        if (followService.isFollowing(loggedInUserID, followingID)) {
            return ResponseEntity.status(400).body("You are already following this user.");
        }

        followService.addFollow(loggedInUserID, followingID);
        return ResponseEntity.status(200).body("Successfully followed.");
    }

    @GetMapping("/follows")
    public ResponseEntity<List<Map<String, Object>>> getFollows(
            @CookieValue("Authentication") String bearerToken,
            @RequestParam int userID,
            @RequestParam String type) {
        Integer loggedInUserId = tokenService.returnAuthID(bearerToken);
        if (loggedInUserId == null || !loggedInUserId.equals(userID)) {
            return ResponseEntity.status(403).body(null);
        }

        List<Follow> follows;
        if ("followers".equalsIgnoreCase(type)) {
            follows = followService.findByFollower(userID);
        } else if ("following".equalsIgnoreCase(type)) {
            follows = followService.findByFollowing(userID);
        } else {
            return ResponseEntity.status(400).body(null);
        }

        List<Map<String, Object>> response = follows.stream().map(follow -> {
            Map<String, Object> followMap = new HashMap<>();
            followMap.put("followerUsername", follow.getFollower().getUsername());
            followMap.put("followerBio", follow.getFollower().getBio());
            followMap.put("followingUsername", follow.getFollowing().getUsername());
            followMap.put("followingBio", follow.getFollowing().getBio());
            return followMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows/follower/{followerUserID}")
    public ResponseEntity<List<Map<String, Object>>> getFollowsByFollowerId(@CookieValue("Authentication") String bearerToken, @PathVariable int followerUserID) {
        Integer loggedInUserId = tokenService.returnAuthID(bearerToken);
        if (loggedInUserId == null || !loggedInUserId.equals(followerUserID)) {
            return ResponseEntity.status(403).body(null);
        }

        List<Follow> follows = followService.findByFollower(followerUserID);
        List<Map<String, Object>> response = follows.stream().map(follow -> {
            Map<String, Object> followMap = new HashMap<>();
            followMap.put("followerUsername", follow.getFollower().getUsername());
            followMap.put("followerBio", follow.getFollower().getBio());
            followMap.put("followingUsername", follow.getFollowing().getUsername());
            followMap.put("followingBio", follow.getFollowing().getBio());
            return followMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows/following/{followingUserID}")
    public ResponseEntity<List<Map<String, Object>>> getFollowsByFollowingId(@CookieValue("Authentication") String bearerToken, @PathVariable int followingUserID) {
        Integer loggedInUserId = tokenService.returnAuthID(bearerToken);
        if (loggedInUserId == null || !loggedInUserId.equals(followingUserID)) {
            return ResponseEntity.status(403).body(null);
        }

        List<Follow> follows = followService.findByFollowing(followingUserID);
        List<Map<String, Object>> response = follows.stream().map(follow -> {
            Map<String, Object> followMap = new HashMap<>();
            followMap.put("followerUsername", follow.getFollower().getUsername());
            followMap.put("followerBio", follow.getFollower().getBio());
            followMap.put("followingUsername", follow.getFollowing().getUsername());
            followMap.put("followingBio", follow.getFollowing().getBio());
            return followMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/follows/check")
    public ResponseEntity<Boolean> isFollowerIDFollowingFollowingID(@CookieValue("Authentication") String bearerToken, @RequestParam int followerID, @RequestParam int followingID) {
        Integer loggedInUserID = tokenService.returnAuthID(bearerToken);
        if (loggedInUserID == null || !loggedInUserID.equals(followerID)) {
            return ResponseEntity.status(403).body(null);
        }

        boolean isFollowing = followService.isFollowing(followerID, followingID);
        return ResponseEntity.status(200).body(isFollowing);
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<String> unfollow(@CookieValue("Authentication") String bearerToken, @RequestParam int followerID, @RequestParam int followingID) {
        Integer loggedInUserID = tokenService.returnAuthID(bearerToken);
        if (loggedInUserID == null || !loggedInUserID.equals(followerID)) {
            return ResponseEntity.status(403).body("Unauthorized to unfollow.");
        }

        User follower = userService.getUser(followerID);
        User following = userService.getUser(followingID);

        if (follower == null || following == null) {
            return ResponseEntity.status(404).body("User not found.");
        }

        if (!followService.isFollowing(followerID, followingID)) {
            return ResponseEntity.status(404).body("Not following this user.");
        }

        followService.unfollow(followerID, followingID);
        return ResponseEntity.status(200).body("Successfully unfollowed.");
    }

    @PostMapping("/chats")
    public ResponseEntity<Chat> createChat(@RequestBody Chat chat) {
        Chat savedChat = chatService.createChat(chat);
        notificationService.createNotification(chat.getReceiver().getUserID(), "You have a new message from " + chat.getSender().getUserID());
        return ResponseEntity.status(201).body(savedChat);
    }

    @GetMapping("/chats/get")
    public ResponseEntity<List<Chat>> getAllChats() {
        List<Chat> chats = chatService.getAllChats();
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/notifications/unread/{userID}")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable int userID) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userID);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/notifications/markAsRead/{id}")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.noContent().build();
    }
}