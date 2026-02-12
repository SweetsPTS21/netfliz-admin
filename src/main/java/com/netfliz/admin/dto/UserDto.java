package com.netfliz.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netfliz.admin.constant.enums.Role;
import com.netfliz.admin.constant.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank(message = "Username không được để trống")
    private String username;
    @NotBlank(message = "First name không được để trống")
    private String firstName;
    @NotBlank(message = "Last name không được để trống")
    private String lastName;
    @NotBlank(message = "Email không được để trống")
    private String email;
    private String phone;
    @NotNull(message = "Role không được để trống")
    private Role role;
    @NotNull(message = "Status không được để trống")
    private UserStatus status;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime updatedAt;
}
