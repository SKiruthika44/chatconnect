package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepo extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

    List<Users> findByUsernameContainingIgnoreCase(String keyword);
}
