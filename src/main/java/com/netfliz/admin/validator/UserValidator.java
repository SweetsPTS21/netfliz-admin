package com.netfliz.admin.validator;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.exception.BadRequestException;
import com.netfliz.admin.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserValidator {
    private final UserRepository userRepository;

    public void validateCreateUser(UserDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByPhone(user.getPhone())) {
            throw new BadRequestException("Phone already exists");
        }
    }
}
