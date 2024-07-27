package com.revature.RevConnect.repositories;

import com.revature.RevConnect.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUserID(int id);
    User findByUsername(String username);
}
