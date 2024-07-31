package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByReceiverUserID(int receiverId);
    List<Chat> findBySenderUserID(int senderId);
    List<Chat> findAllBySenderUserIDOrReceiverUserID(int senderId, int receiverId);
    List<Chat> findBySenderUserIDAndReceiverUserID(int senderId, int receiverId);
    List<Chat> findByReceiverUserIDAndSenderUserID(int receiverId, int senderId);

    @Query("SELECT c FROM Chat c WHERE (c.sender.userID = :user1ID AND c.receiver.userID = :user2ID) OR (c.sender.userID = :user2ID AND c.receiver.userID = :user1ID) ORDER BY c.createdAt")
    List<Chat> findAllBySenderAndReceiver(@Param("user1ID") int user1ID, @Param("user2ID") int user2ID);
}
