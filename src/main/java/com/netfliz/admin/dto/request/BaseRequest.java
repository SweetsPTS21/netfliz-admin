package com.netfliz.admin.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public abstract class BaseRequest {
    @Min(value = 1, message = "Số trang phải lớn hơn 1")
    private Integer page = 1;

    @Min(value = 10, message = "Số lượng phẩn tử trong trang phải lớn hơn 10")
    private Integer pageSize = 20;
}
