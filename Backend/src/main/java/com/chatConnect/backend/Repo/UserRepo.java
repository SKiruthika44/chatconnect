package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface UserRepo extends JpaRepository<Users,Long> {
    Users findByUsername(String username);

    List<Users> findByUsernameContainingIgnoreCase(String keyword);


    @Query(value= """
            select u
            from Message m
            join Users u
            on u.id=case
            when m.sender.id=:userId then m.receiver.id
            else m.sender.id
            end
            where m.sender.id=:userId or m.receiver.id=:userId
            group by u.id
            
            
             """)
    List<Users> findRecentUsers(@Param("userId")long userId);
}
