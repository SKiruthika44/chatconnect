package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatRepo extends JpaRepository<GroupChat,Long> {
    GroupChat findByGroupName(String groupId);

    @Query(value="Select g from GroupChat g where g.id=:groupId ")

    GroupChat findByGroupId(@Param("groupId")Long groupId);
}
