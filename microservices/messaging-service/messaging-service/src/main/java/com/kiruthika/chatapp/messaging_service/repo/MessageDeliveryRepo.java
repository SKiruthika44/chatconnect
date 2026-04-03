package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.MessageDelivery;
import com.kiruthika.chatapp.messaging_service.modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface MessageDeliveryRepo extends JpaRepository<MessageDelivery, MessageUserId> {

}
