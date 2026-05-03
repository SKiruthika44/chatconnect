package com.kiruthika.chatapp.messaging_service.repo;


import com.kiruthika.chatapp.messaging_service.modal.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepo extends JpaRepository<Message,Long> {

    Message findByMsgId(long msgId);
}
