package com.netfliz.admin.service;

import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.dto.MovieMetadataDto;
import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;

import java.util.List;

public interface MovieService {

    /**
     * Lấy danh sách phim theo filter
     */
    PageResponse<MovieDto> getMoviesByFilter(MovieFilterRequest request);

    /**
     * Lấy thông tin phim theo id
     *
     * @param id movieId
     * @return MovieDto
     */
    MovieDto getMovieById(Long id);

    /**
     * Thêm mới phim
     *
     * @param request MovieDto
     * @return MovieDto
     */
    MovieDto createMovie(MovieDto request);

    /**
     * Cập nhật phim
     *
     * @param id      movieId
     * @param request MovieDto
     * @return MovieDto
     */
    MovieDto updateMovie(Long id, MovieDto request);

    /**
     * Xóa phim (hard) và các image, asset
     *
     * @param id movieId
     */
    void deleteMovie(Long id);

    /**
     * Insert multiple movies
     *
     * @param movies list of movies
     * @return number of inserted movies
     */
    Integer bulkMovie(List<MovieDto> movies);

    /**
     * Get movie metadata
     */
    MovieMetadataDto getMovieMetadata();
}
