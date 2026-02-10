package com.netfliz.admin.repository;

import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.entity.MovieEntity;
import org.springframework.data.domain.Page;

public interface CustomMovieRepository {
    /**
     * Find movies by filter
     *
     * @param request MovieFilterRequest
     * @return Page<MovieEntity>
     */
    Page<MovieEntity> findByFilter(MovieFilterRequest request);
}
