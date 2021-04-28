package com.suri.spring_test_practice;

import com.suri.spring_test_practice.dto.CreateUserRequest;
import com.suri.spring_test_practice.dto.CreateUserResponse;
import com.suri.spring_test_practice.dto.GetUserListResponse;
import com.suri.spring_test_practice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public CreateUserResponse createUser(CreateUserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/users")
    public GetUserListResponse getUserList() {
        return userService.getUserList();
    }

    @GetMapping("/users/{userId}")
    public UserDto getUser(@PathVariable("userId") Long userId) {
        return userService.getUser(userId);
    }
}
