package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.model.Message;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

        @Query("SELECT m FROM messages m WHERE m.conversation.id = :conversationId ORDER BY m.createdDateTime ASC")
        Page<Message> findByConversationId(@Param("conversationId") Integer conversationId, Pageable pageable);
}

