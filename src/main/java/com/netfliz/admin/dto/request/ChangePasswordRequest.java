package com.netfliz.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "User id không được để trống")
    private Long id;
    @NotBlank(message = "Password không được để trống")
    private String password;
}
