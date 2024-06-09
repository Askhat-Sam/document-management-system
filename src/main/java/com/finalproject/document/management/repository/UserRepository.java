package com.finalproject.document.management.repository;

import com.finalproject.document.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value="SELECT * FROM user WHERE user.id = :id", nativeQuery = true)
    User getOldUserById(@Param("id") Long id);
    @Query("SELECT u.userId FROM User u")
    List<String> findAllUserIds();
}
