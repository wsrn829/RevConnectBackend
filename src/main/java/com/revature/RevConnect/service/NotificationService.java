package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Notification;
import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.NotificationRepository;
import com.revature.RevConnect.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    public void createNotification(int userID, String message) {
        User user = userRepository.findById(userID).orElseThrow(() -> new RuntimeException("User not found"));
        Notification notification = new Notification(message, false, LocalDateTime.now(), user);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications(int userID) {
        return notificationRepository.findByUserUserIDAndReadFalse(userID);
    }

    public void markAsRead(Long id) {
        Optional<Notification> optionalNotification = notificationRepository.findById(id);
        if (optionalNotification.isPresent()) {
            Notification notification = optionalNotification.get();
            notification.setRead(true);
            notificationRepository.save(notification);
        } else {
            // Handle the case where the notification with the given id does not exist
            throw new IllegalArgumentException("Notification with ID " + id + " does not exist.");
        }
    }
}
