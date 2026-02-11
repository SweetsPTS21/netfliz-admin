package com.netfliz.admin.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieEpisodeFilterRequest extends BaseRequest {
    @NotNull(message = "Episode id không được để trống")
    private Long id;
}
