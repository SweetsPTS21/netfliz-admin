package com.netfliz.admin.dto;

import com.netfliz.admin.constant.enums.Role;
import com.netfliz.admin.constant.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotBlank(message = "Password không được để trống")
    private String password;
    private String phone;
    @NotNull(message = "Role không được để trống")
    private Role role;
    @NotNull(message = "Status không được để trống")
    private UserStatus status;
}
