package edu.miu.cse.heartlink.repository;

import edu.miu.cse.heartlink.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
    void deleteByName(String name);
}
