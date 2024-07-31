package com.revature.RevConnect.service;

import com.revature.RevConnect.models.Chat;
import com.revature.RevConnect.models.User;
import com.revature.RevConnect.repositories.ChatRepository;
import com.revature.RevConnect.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChatService {


    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Autowired
    public ChatService(UserRepository userRepository, ChatRepository chatRepository) {
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    public List<Chat> getMessagesBetweenUsers(int user1ID, int user2ID) {
        return chatRepository.findAllBySenderAndReceiver(user1ID, user2ID);
    }

    public List<Chat> findAllChats() {
        return chatRepository.findAll();
    }

    public Optional<Chat> getChatById(Long chatId) {
        return chatRepository.findById(chatId);
    }

    public List<Chat> getChatsBySenderId(int senderId) {
        return chatRepository.findBySenderUserID(senderId);
    }

    public List<Chat> getChatsByReceiverId(int receiverId) {
        return chatRepository.findByReceiverUserID(receiverId);
    }

    public Chat createChat(Chat chat) {
        User sender = userRepository.findById(chat.getSender().getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(chat.getReceiver().getUserID())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found. Please check the receiver ID."));

        chat.setSender(sender);
        chat.setReceiver(receiver);

        return chatRepository.save(chat);
    }

    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }
}