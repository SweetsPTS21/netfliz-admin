package com.netfliz.admin.controller.graphql;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class UserGraphql {
    private final UserService userService;

    @QueryMapping
    public PageResponse<UserDto> getAllUser(@Argument UserFilterRequest request) {
        return userService.getAllUser(request);
    }

    @QueryMapping
    public UserDto getUserById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    public UserDto createUser(@Argument UserDto user) {
        return userService.createUser(user);
    }

    @MutationMapping
    public UserDto updateUserById(@Argument Long id, @Argument UserDto user) {
        return userService.updateUser(id, user);
    }

    @MutationMapping
    public Boolean deleteUserById(@Argument Long id) {
        userService.deleteUser(id);
        return true;
    }
}
