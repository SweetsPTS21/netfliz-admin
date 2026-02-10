package com.netfliz.admin.mapper;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.entity.UserEntity;
import com.netfliz.admin.util.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserDto mapToDto(UserEntity from) {
        return UserDto.builder()
                .id(from.getId())
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .username(from.getUsername())
                .email(from.getEmail())
                .phone(from.getPhone())
                .role(from.getRole())
                .status(from.getStatus())
                .build();
    }

    public UserEntity mapToEntity(UserDto from) {
        return UserEntity.builder()
                .username(CommonUtils.generateUsername(from.getEmail()))
                .password(passwordEncoder.encode(from.getPassword()))
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .phone(from.getPhone())
                .role(from.getRole())
                .status(from.getStatus())
                .build();
    }

    public List<UserDto> mapToDtoList(List<UserEntity> from) {
        return from.stream().map(this::mapToDto).toList();
    }
}
