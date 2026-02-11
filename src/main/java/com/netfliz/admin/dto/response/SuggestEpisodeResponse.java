package com.netfliz.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestEpisodeResponse {
    private Integer episodeNumber;
    private Integer episodeOrder;
}
