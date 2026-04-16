package com.kiruthika.chatapp.messaging_service.repo;

import com.kiruthika.chatapp.messaging_service.modal.GroupMember;
import com.kiruthika.chatapp.messaging_service.modal.GroupUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface GroupMemberRepo extends JpaRepository<GroupMember, GroupUserId> {

    @Query(value="select m from GroupMember m where m.id.groupId=:groupId")
    List<GroupMember> findByGroupId(@Param("groupId") Long groupId);


    @Query(value="select m from GroupMember m where m.id.userId=:userId")

    List<GroupMember> findByUserId(@Param("userId")Long userId);
}
