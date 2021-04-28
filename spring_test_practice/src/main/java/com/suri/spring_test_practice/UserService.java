package com.suri.spring_test_practice;


import com.suri.spring_test_practice.dto.CreateUserRequest;
import com.suri.spring_test_practice.dto.CreateUserResponse;
import com.suri.spring_test_practice.dto.GetUserListResponse;
import com.suri.spring_test_practice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public CreateUserResponse createUser(CreateUserRequest request) {
        UserEntity newUser = UserEntity.builder()
                .userName(request.getUserName())
                .build();

        Long userId = userRepository.save(newUser).getUserId();
        return new CreateUserResponse(userId);
    }

    public GetUserListResponse getUserList() {
        return new GetUserListResponse(userRepository.findAll().stream()
                .map(user -> new UserDto(user.getUserName()))
                .collect(Collectors.toList()));
    }

    public UserDto getUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다."));

        return new UserDto(userEntity.getUserName());
    }


}
