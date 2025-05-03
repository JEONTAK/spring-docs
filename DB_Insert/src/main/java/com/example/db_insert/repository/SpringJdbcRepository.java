package com.example.db_insert.repository;

import com.example.db_insert.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface SpringJdbcRepository extends CrudRepository<User, Long> {

}
