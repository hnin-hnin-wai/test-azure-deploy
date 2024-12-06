package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.model.ItemClaimTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemClaimTransactionRepository extends JpaRepository<ItemClaimTransaction, Integer> {

    // Custom query to find transactions by user ID
    @Query("SELECT t FROM itemrequestransactions t WHERE t.itemRequest.receiver.userId = :userId OR t.itemRequest.donar.userId = :userId")
    List<ItemClaimTransaction> findAllByUserId(@Param("userId") Integer userId);
}
