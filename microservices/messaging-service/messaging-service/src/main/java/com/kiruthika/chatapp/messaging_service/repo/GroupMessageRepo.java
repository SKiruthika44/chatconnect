package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface GroupMessageRepo extends JpaRepository<GroupMessage,Long> {
}
