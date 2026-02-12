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
                .createdAt(CommonUtils.instantToLocalDateTime(from.getCreatedAt()))
                .updatedAt(CommonUtils.instantToLocalDateTime(from.getUpdatedAt()))
                .build();
    }

    public UserEntity createUserEntity(UserDto from) {
        return UserEntity.builder()
                .username(CommonUtils.generateUsername(from.getEmail()))
                .password(passwordEncoder.encode(CommonUtils.generatePassword()))
                .firstName(from.getFirstName())
                .lastName(from.getLastName())
                .email(from.getEmail())
                .phone(from.getPhone())
                .role(from.getRole())
                .status(from.getStatus())
                .createdAt(CommonUtils.localDateTimeToInstant(from.getCreatedAt()))
                .updatedAt(CommonUtils.localDateTimeToInstant(from.getUpdatedAt()))
                .build();
    }

    public void mapDtoToEntity(UserDto from, UserEntity to) {
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setPhone(from.getPhone());
        to.setRole(from.getRole());
        to.setStatus(from.getStatus());
    }

    public List<UserDto> mapToDtoList(List<UserEntity> from) {
        return from.stream().map(this::mapToDto).toList();
    }
}
