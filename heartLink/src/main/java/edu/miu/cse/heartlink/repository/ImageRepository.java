package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.model.Image;
import edu.miu.cse.heartlink.model.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query("SELECT i FROM images i where i.item.id= :itemId")
    List<Image> findByItemId(@Param("itemId")Integer itemId);

    @Modifying // Required for DELETE or UPDATE queries
    @Transactional // Ensures the query runs within a transaction
    @Query("DELETE FROM images i where i.item.id= :itemId")
    void deleteByItemId(@Param("itemId")Integer itemId);

}
