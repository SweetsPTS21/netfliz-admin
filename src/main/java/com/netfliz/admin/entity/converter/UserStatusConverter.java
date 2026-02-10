package com.netfliz.admin.entity.converter;

import com.netfliz.admin.constant.enums.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

@Converter(autoApply = true)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserStatus userStatus) {
        return Objects.isNull(userStatus) ? null : userStatus.getId();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer integer) {
        return Objects.isNull(integer) ? null : UserStatus.fromId(integer);
    }
}
