package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Message;
import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    // Create a new message
    public void addMessage(Message message) {
        messageRepository.save(message);
    }

    // Find all messages ordered by timestamp
    public List<Message> findAllMessages() {
        return messageRepository.findAllByOrderByTimestampAsc();
    }

    // Find all messages sent by a specific user
    public List<Message> findBySender(User sender) {
        return messageRepository.findBySender(sender);
    }

    // Check if a message exists by its ID
    public boolean existsById(Long messageID) {
        return messageRepository.existsById(messageID);
    }

    // Delete a message by its ID
    public void deleteMessage(Long messageID) {
        messageRepository.deleteById(messageID);
    }

    // Find messages between two users
    public List<Message> findMessagesBetweenUsers(User sender, User receiver) {
        return messageRepository.findBySenderAndReceiverOrderByTimestampAsc(sender, receiver);
    }
}
