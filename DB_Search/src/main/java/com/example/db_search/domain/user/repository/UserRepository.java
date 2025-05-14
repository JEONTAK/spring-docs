package com.example.db_search.domain.user.repository;

import com.example.db_search.domain.user.dto.response.UserQueryResponse;
import com.example.db_search.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByName(String name);

    List<User> findByName(String name);

    @Query("SELECT new com.example.db_search.domain.user.dto.response.UserQueryResponse(u.id, u.email) FROM User u WHERE u.name = :name")
    List<UserQueryResponse> findIdEmailByName(String name);

    List<User> findAllByNameIn(List<String> names);

    Page<User> findAll(Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email%")
    List<User> findAllByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.indexingEmail LIKE :indexingEmail%")
    List<User> findAllByIndexingEmail(String indexingEmail);

}
