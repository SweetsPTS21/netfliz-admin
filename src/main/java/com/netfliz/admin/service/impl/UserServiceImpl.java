package com.netfliz.admin.service.impl;

import com.netfliz.admin.dto.UserDto;
import com.netfliz.admin.dto.request.UserFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.entity.UserEntity;
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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    public PageResponse<UserDto> getAllUser(UserFilterRequest request) {
        PageRequest pageable = PageRequest.of(request.getPage(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "updatedAt"));
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        List<UserDto> items = userMapper.mapToDtoList(userPage.getContent());

        return PageResponse.<UserDto>builder()
                .page(userPage.getNumber())
                .pageSize(userPage.getSize())
                .total(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .items(items)
                .build();
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.mapToDto(userEntity);
    }

    @Override
    public UserDto createUser(@Valid UserDto userDto) {
        userValidator.validateCreateUser(userDto);

        UserEntity userEntity = userMapper.mapToEntity(userDto);
        return userMapper.mapToDto(userRepository.save(userEntity));
    }

    @Override
    public UserDto updateUser(Long id, @Valid UserDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
