package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Message;
import com.revature.RevConnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // Retrieve all messages ordered by timestamp
    List<Message> findAllByOrderByTimestampAsc();

    // Find all messages sent by a specific user
    List<Message> findBySender(User sender);

    // Check if a message exists by its ID
    boolean existsById(Long messageID);

    // Delete a message by its ID
    void deleteById(Long messageID);

    // Find messages between two users ordered by timestamp
    List<Message> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);
}
