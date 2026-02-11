package com.netfliz.admin.service;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.ChangePasswordRequest;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;

public interface UserService {

    /**
     * Lấy danh sách user theo filter
     */
    PageResponse<UserDto> getAllUser(UserFilterRequest request);

    /**
     * Lấy user theo id
     */
    UserDto getUserById(Long id);

    /**
     * Tạo mới user.
     */
    UserDto createUser(UserDto userDto);

    /**
     * Cập nhật user.
     */
    UserDto updateUser(Long id, UserDto userDto);

    /**
     * Xóa người dùng (soft delete).
     */
    void deleteUser(Long id);

    /**
     * Disable user.
     */
    void disableUser(Long id);

    /**
     * Đổi mật khẩu
     */
    void changePassword(ChangePasswordRequest request);
}
