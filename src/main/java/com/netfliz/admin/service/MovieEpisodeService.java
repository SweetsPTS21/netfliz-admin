package com.netfliz.admin.service;

import com.netfliz.admin.dto.MovieEpisodeDto;
import com.netfliz.admin.dto.request.MovieEpisodeFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.dto.response.SuggestEpisodeResponse;

public interface MovieEpisodeService {
    /**
     * Lấy danh sách tập phim
     */
    PageResponse<MovieEpisodeDto> getMovieEpisodes(MovieEpisodeFilterRequest request);

    /**
     * Cập nhật tập phim
     */
    MovieEpisodeDto updateMovieEpisode(Long id, MovieEpisodeDto movieEpisode);

    /**
     * Gợi ý số tập tiếp theo
     */
    SuggestEpisodeResponse suggestEpisodeNumber(Long id);

    /**
     * Xóa tập phim
     *
     * @param movieId        movieId
     * @param movieEpisodeId movieEpisodeId
     * @return MovieEpisodeDto
     */
    MovieEpisodeDto deleteMovieEpisode(Long movieId, Long movieEpisodeId);
}
