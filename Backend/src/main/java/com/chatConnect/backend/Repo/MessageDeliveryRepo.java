package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.MessageUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageDeliveryRepo extends JpaRepository<MessageDelivery, MessageUserId> {

    @Query(value="select count(m) from MessageDelivery m where m.id.messageId =:msgId ")

   long countByMsgIdAndUserId(@Param("msgId")Long msgId);

    @Query(value="select count(m)>0 from MessageDelivery m where m.id.messageId =:msgId and m.id.userId=:userId ")

    boolean existsByMsgIdAndUserIdByCount(@Param("msgId")Long msgId, @Param("userId")Long userId);
}
