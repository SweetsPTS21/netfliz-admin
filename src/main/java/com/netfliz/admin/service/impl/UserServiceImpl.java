package com.netfliz.admin.service.impl;

import com.netfliz.admin.constant.enums.UserStatus;
import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.ChangePasswordRequest;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.entity.UserEntity;
import com.netfliz.admin.exception.BadRequestException;
import com.netfliz.admin.exception.NotFoundException;
import com.netfliz.admin.mapper.UserMapper;
import com.netfliz.admin.repository.UserRepository;
import com.netfliz.admin.service.UserService;
import com.netfliz.admin.validator.UserValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PageResponse<UserDto> getAllUser(UserFilterRequest request) {
        PageRequest pageable = PageRequest.of(
                request.getPage() - 1,
                request.getPageSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        List<UserDto> items = userMapper.mapToDtoList(userPage.getContent());

        return PageResponse.<UserDto>builder()
                .page(userPage.getNumber() + 1)
                .pageSize(userPage.getSize())
                .total(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User không tồn tại"));
        return userMapper.mapToDto(userEntity);
    }

    @Override
    @Transactional
    public UserDto createUser(@Valid UserDto userDto) {
        userValidator.validateCreateUser(userDto);

        UserEntity userEntity = userMapper.createUserEntity(userDto);
        return userMapper.mapToDto(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, @Valid UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User không tồn tại"));
        userMapper.mapDtoToEntity(userDto, userEntity);

        return userMapper.mapToDto(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User không tồn tại"));

        if (!userEntity.getStatus().equals(UserStatus.DELETED)) {
            userEntity.setStatus(UserStatus.DELETED);
            userRepository.save(userEntity);
        } else {
            throw new IllegalStateException("User không tồn tại");
        }
    }

    @Override
    public void disableUser(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User không tồn tại"));

        if (!userEntity.getStatus().equals(UserStatus.ACTIVE)) {
            throw new IllegalStateException("Trạng thái user không hợp lệ");
        }

        userEntity.setStatus(UserStatus.INACTIVE);
        userRepository.save(userEntity);
    }

    @Override
    public void changePassword(@Valid ChangePasswordRequest request) {
        UserEntity userEntity = userRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("User không tồn tại"));

        if (passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new BadRequestException("Mật khẩu mới không trùng mật khẩu cũ");
        }

        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(userEntity);
    }
}
