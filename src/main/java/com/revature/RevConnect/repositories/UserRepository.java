package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserID(int id);
    User findByUsername(String username);
    List<User> findByUsernameContainingIgnoreCase(String query);
}
