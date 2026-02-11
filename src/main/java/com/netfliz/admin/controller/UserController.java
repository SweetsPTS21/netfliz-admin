package com.netfliz.admin.controller;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.ChangePasswordRequest;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/filter")
    public ResponseEntity<PageResponse<UserDto>> getAllUser(@RequestBody @Valid UserFilterRequest request) {
        return ResponseEntity.ok(userService.getAllUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping("")
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserDto user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Xóa user thành công");
    }

    @PostMapping("/{id}/disable")
    public ResponseEntity<Object> disableUser(@PathVariable Long id) {
        userService.disableUser(id);
        return ResponseEntity.ok("Disable user thành công");
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return ResponseEntity.ok("Đổi mật khẩu thành công");
    }
}
