package com.chatConnect.backend.Repo;

import com.chatConnect.backend.Modal.GroupChat;
import com.chatConnect.backend.Modal.GroupMessage;
import com.chatConnect.backend.Modal.GroupMessageEmojiReaction;
import com.chatConnect.backend.Modal.Users;
//import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.transaction.annotation.Propagation;

import java.util.List;


@Repository
public interface GroupMessageRepo extends JpaRepository<GroupMessage,Long> {


    List<GroupMessage> findByGroupChatAndDeliveredUsersNotContaining(GroupChat groupChat, Users receiver);

    List<GroupMessage> findByGroupChatAndReadUsersNotContaining(GroupChat group, Users user);

    List<GroupMessage> findByGroupChat(GroupChat group);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Modifying
    @Query(value="Insert Ignore into `read-users` (`groupmessage-id`,`user-id`) values(:msgId,:userId)",nativeQuery = true)

    void addReadUser(@Param("msgId")Long msgId, @Param("userId")Long userId);

    @Query(value= """
            select m from GroupMessage m where (m.groupChat=:groupchat) and  not exists (select 1 from GroupMessageDeletedUser d where d.id.userId=:userId and d.id.messageId=m.id) order by m.createdAt asc
            """)
    List<GroupMessage> findAllVisibleMessagesByUser(@Param("user")Users user,@Param("groupchat")GroupChat groupChat,@Param("userId")Long userId);


    @Query(value="select exists (select 1 from GroupMessageEmojiReaction r where r.id.messageId=:msgId and r.id.userId=:userId)")
    Boolean existsByMsgIdAndUserId(@Param("msgId")Long msgId,@Param("userId") Long userId);

    @Query(value="select r from GroupMessageEmojiReaction r where r.id.messageId=:msgId and r.id.userId=:userId")

    GroupMessageEmojiReaction findByMsgIdAndUserId(@Param("msgId")Long msgId,@Param("userId") Long userId);

    @Query(value="select r.emoji,count(*) from GroupMessageEmojiReaction r where r.id.messageId=:msgId group by r.emoji")
    List<Object[]> findEmojisCountForMessage(@Param("msgId")Long msgId);
}
