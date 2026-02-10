package com.netfliz.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private Integer page;
    private Integer pageSize;
    private Long total;
    private Integer totalPages;
    private List<T> items;
}
