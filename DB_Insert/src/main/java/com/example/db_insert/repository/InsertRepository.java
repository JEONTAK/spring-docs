package com.example.db_insert.repository;


import com.example.db_insert.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsertRepository extends JpaRepository<User, Long> {

}
