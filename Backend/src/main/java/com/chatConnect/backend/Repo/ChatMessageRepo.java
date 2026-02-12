package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.ChatMessage;
import com.chatConnect.backend.Modal.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Long> {


    List<ChatMessage> findBySenderAndReceiver(Users user, Users otherUser, Sort sortOption);

    List<ChatMessage> findByReceiverAndIsReadFalse(Users username);



    List<ChatMessage> findByReceiverAndSenderAndIsReadFalse(Users receiver, Users sender);

    List<ChatMessage> findByReceiverAndDeliveredFalse(Users receiver);


}
