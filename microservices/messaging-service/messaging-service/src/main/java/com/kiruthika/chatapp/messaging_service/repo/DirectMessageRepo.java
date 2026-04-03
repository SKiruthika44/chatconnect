package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.DirectMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DirectMessageRepo extends JpaRepository<DirectMessage,Long> {

}
