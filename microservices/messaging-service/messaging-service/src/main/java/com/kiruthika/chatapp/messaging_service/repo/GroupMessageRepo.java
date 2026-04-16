package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface GroupMessageRepo extends JpaRepository<GroupMessage,Long> {
    List<GroupMessage> findByGroupId(Long groupId);
}
