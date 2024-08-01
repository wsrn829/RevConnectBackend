package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.revature.RevConnect.models.Notification;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserUserIDAndReadFalse(int userID);

    Optional<Notification> findById(Long id);
}
