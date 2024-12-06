package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.model.Item;
import edu.miu.cse.heartlink.model.ItemClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemClaimRepository extends JpaRepository<ItemClaim, Integer> {

    // Custom query to find requests by itemId
    @Query("SELECT ir FROM itemrequests ir WHERE ir.item.id = :itemId")
    List<ItemClaim> findByItemId(@Param("itemId") long itemId);


    @Query("SELECT ir.item FROM itemrequests ir WHERE ir.receiver.userId = :receiverId AND ir.status = 'ACCEPTED'")
    List<Item> findAllAcceptedItemsByReceiverId(@Param("receiverId") Integer receiverId);


}
