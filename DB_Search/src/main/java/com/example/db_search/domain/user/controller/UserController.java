package com.example.db_search.domain.user.controller;

import com.example.db_search.common.annotation.ResponseTime;
import com.example.db_search.common.dto.PageResponse;
import com.example.db_search.common.exception.CustomException;
import com.example.db_search.domain.order.dto.response.OrderResponse;
import com.example.db_search.domain.user.dto.response.UserQueryResponse;
import com.example.db_search.domain.user.dto.response.UserResponse;
import com.example.db_search.domain.user.dto.response.UserWithOrderResponse;
import com.example.db_search.domain.user.entity.User;
import com.example.db_search.domain.user.service.UserCommandService;
import com.example.db_search.domain.user.service.UserQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 삽입 및 다양한 조회 관련 API")
@ResponseTime
public class UserController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @Operation(summary = "1. 유저 삽입", description = "100만건의 유저를 랜덤으로 INSERT 합니다.")
    @PostMapping("/init")
    public ResponseEntity<Void> initUsers() {
        userCommandService.initUsers();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "2. 기본 방식의 유저 조회 (이메일)", description = "해당 이메일 문자열이 포함되어 있는 유저들을 기본 방식으로 조회합니다.")
    @GetMapping("/email/default")
    public ResponseEntity<List<UserResponse>> getUsersUsingEmail(@RequestParam(name = "email", defaultValue = "user_000") String email) {
        List<User> users = userQueryService.findAllByEmail(email);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "3. 인덱싱을 사용한 유저 조회", description = "해당 이메일 문자열이 포함되어 있는 유저들을 인덱싱을 사용한 방식으로 조회합니다.")
    @GetMapping("/email/indexing")
    public ResponseEntity<List<UserResponse>> getUsersUsingIndexing(@RequestParam(name = "email", defaultValue = "user_000") String email) {
        List<User> users = userQueryService.findAllUsingIndexing(email);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "4. 기본 방식의 유저 조회 (이름)", description = "이름과 일치하는 유저들을 기본 방식으로 조회합니다.")
    @GetMapping("/name/default")
    public ResponseEntity<List<UserResponse>> getUsersUsingName(@RequestParam(name = "name", defaultValue = "Name_0") String name) {
        List<User> users = userQueryService.findAllByName(name);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "5. 쿼리 최적화를 사용한 유저 조회 (필요한 컬럼만 조회)", description = "해당 이메일 문자열이 포함되어 있는 유저들을 쿼리 최적화를 사용한 방식으로 조회합니다.")
    @GetMapping("/name/query")
    public ResponseEntity<List<UserQueryResponse>> getUsersUsingOptimizeQuery(@RequestParam(name = "name", defaultValue = "Name_0") String name) {
        List<UserQueryResponse> responses = userQueryService.findAllUsingQuery(name);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "6. 쿼리 최적화를 사용한 유저 조회 (페이징)", description = "페이징을 활용한 방식으로 유저를 조회합니다.")
    @GetMapping("/name/paging")
    public ResponseEntity<PageResponse<UserResponse>> getUsersUsingOptimizeQuery(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userQueryService.findAllUsingPageable(pageable);
        Page<UserResponse> responses = users.map(user ->
                UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge()));
        return ResponseEntity.ok(PageResponse.of(responses));
    }

    @Operation(summary = "7. 배치 조회를 사용한 유저 조회 (IN)", description = "배치 조회(IN)을 활용한 방식으로 유저를 조회합니다.")
    @GetMapping("/name/batchWithIn")
    public ResponseEntity<List<UserResponse>> getUsersUsingOptimizeQuery(@RequestParam(name = "name", defaultValue = "Name_0, Name_1") List<String> names) {
        List<User> users = userQueryService.findAllByNameIn(names);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "8. 기본 방식을 사용한 다건 유저 조회 ", description = "기본 방식으로 다건 유저를 조회합니다.")
    @GetMapping("/name/usersList")
    public ResponseEntity<List<UserResponse>> getUsersWithList(@RequestParam(name = "name", defaultValue = "Name_0, Name_1") List<String> names) {
        List<User> users = userQueryService.findAllWithList(names);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "9. Redis Cache를 사용한 유저 조회", description = "Redis Cache를 활용한 방식으로 유저를 조회합니다.")
    @GetMapping("/name/rediscache")
    public ResponseEntity<List<UserResponse>> getUsersUsingRedisCache(@RequestParam(name = "name", defaultValue = "Name_0") String name) {
        List<User> users = userQueryService.findAllUsingRedisCached(name);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "10. Caffeine Cache를 사용한 유저 조회", description = "Caffeine Cache를 활용한 방식으로 유저를 조회합니다.")
    @GetMapping("/name/caffeine")
    public ResponseEntity<List<UserResponse>> getUsersUsingCaffeineCache(@RequestParam(name = "name", defaultValue = "Name_0") String name) {
        List<User> users = userQueryService.findAllUsingCaffeineCached(name);
        List<UserResponse> responses = users.stream().map(user -> {
            return UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge());
        }).toList();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "11. Async를 사용한 유저 조회", description = "Caffeine Cache를 활용한 방식으로 유저를 조회합니다.")
    @GetMapping("/name/async")
    public ResponseEntity<List<UserResponse>> getUsersUsingAsync(@RequestParam(name = "name", defaultValue = "Name_0") String name) {
        CompletableFuture<List<User>> usersFuture = userQueryService.findAllUsingAsync(name);

        try {
            List<User> users = usersFuture.get();

            List<UserResponse> responses = users.stream()
                    .map(user -> UserResponse.create(user.getId(), user.getEmail(), user.getName(), user.getAge()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "비동기 처리 중 에러가 발생했습니다.");
        }
    }

}
