package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.dto.response.ConversationResponseDto;
import edu.miu.cse.heartlink.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
//    List<ConversationResponseDto> findByReceiverOrderByCreatedDate(Integer userId);
//    List<ConversationResponseDto> findBySender(Integer userId);

    @Query("SELECT c FROM conversations c WHERE c.sender.id = :senderId AND c.receiver.id = :receiverId AND c.item.id = :itemId")
    Optional<Conversation> findBySenderReceiverAndItem(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId, @Param("itemId") Integer itemId);

}
