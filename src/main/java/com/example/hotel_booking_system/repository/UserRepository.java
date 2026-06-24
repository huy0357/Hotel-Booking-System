package com.example.hotel_booking_system.repository;

import com.example.hotel_booking_system.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Long> {


    @Query("""
    SELECT u
    FROM User u
    WHERE (:keyword IS NULL 
           OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))
   
""")
    Page<User> search(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
