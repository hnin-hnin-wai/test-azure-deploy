package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.dto.response.ItemResponseDto;
import edu.miu.cse.heartlink.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    // Custom query to find all items by userId
    @Query("SELECT i FROM items i WHERE i.user.userId = :userId")
    List<Item> findAllByUserId(@Param("userId") Integer userId);
}
