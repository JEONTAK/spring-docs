package com.example.db_search.domain.user.service;

import com.example.db_search.domain.user.dto.response.UserQueryResponse;
import com.example.db_search.domain.user.entity.User;
import com.example.db_search.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    // 기본 조회 (이름)
    public List<User> findAllByName(String name) {
        return userRepository.findAllByName(name);
    }

    // 기본 조회 (이메일)
    public List<User> findAllByEmail(String email) {
        return userRepository.findAllByEmail(email);
    }

    // 인덱싱을 활용한 조회
    public List<User> findAllUsingIndexing(String indexingEmail) {
        return userRepository.findAllByIndexingEmail(indexingEmail);
    }

    // 쿼리를 사용한 조회 (필요한 컬럼만 조회)
    public List<UserQueryResponse> findAllUsingQuery(String name) {
        return userRepository.findIdEmailByName(name);
    }

    // Pageable을 사용한 조회
    public Page<User> findAllUsingPageable(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    //Redis Cache를 사용한 조회
    @Cacheable(value = "redis-users", key = "#name", cacheManager = "redisCacheManager")
    public List<User> findAllUsingRedisCached(String name) {
        System.out.println("👉 DB에서 직접 조회됨: " + name);
        return userRepository.findByName(name);
    }

    //Caffeine Cache를 사용한 조회
    @Cacheable(value = "caffeine-users", key = "#name", cacheManager = "caffeineCacheManager")
    public List<User> findAllUsingCaffeineCached(String name) {
        return userRepository.findByName(name);
    }

    // 비동기 처리를 사용한 조회
    @Async
    public CompletableFuture<List<User>> findAllUsingAsync(String name) {
        return CompletableFuture.supplyAsync(() -> userRepository.findByName(name));
    }

    // IN을 사용한 배치 조회
    public List<User> findAllByNameIn(List<String> names) {
        return userRepository.findAllByNameIn(names);
    }

    public List<User> findAllWithList(List<String> names) {
        return names.stream().flatMap(name -> userRepository.findByName(name).stream()).toList();
    }
}
