package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM user u INNER JOIN document d ON u.id = d.author_id WHERE u.id = :userId", nativeQuery = true)
    User findUserWithDocuments(@Param("userId") Long userId);
}
