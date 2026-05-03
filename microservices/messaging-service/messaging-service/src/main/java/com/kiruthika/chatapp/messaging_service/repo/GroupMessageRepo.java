package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface GroupMessageRepo extends JpaRepository<GroupMessage,Long> {
    List<GroupMessage> findByGroupId(Long groupId);

    GroupMessage findByMsgId(Long msgId);

    @Query(value="Select m from GroupMessage m where m.groupId=:groupId and not exists (select 1 from MessageDeletion d where d.id.messageId=m.msgId and d.id.userId=:userId) order by m.createdAt asc")
    List<GroupMessage> findAllVisibleGroupMessages(@Param("groupId")Long groupId, @Param("userId")Long userId);
}
