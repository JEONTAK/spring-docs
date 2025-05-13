package com.example.db_search.domain.user.service;

import com.example.db_search.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

}
