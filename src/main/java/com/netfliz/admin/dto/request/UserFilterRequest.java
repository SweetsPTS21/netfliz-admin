package com.netfliz.admin.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFilterRequest extends BaseRequest {
    private String username;
    private String email;
    private String phone;
}
