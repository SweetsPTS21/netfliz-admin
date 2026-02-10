package com.netfliz.admin.service;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;

public interface UserService {
    PageResponse<UserDto> getAllUser(UserFilterRequest request);

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);
}
