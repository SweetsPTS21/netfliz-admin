package com.netfliz.admin.repository;

import com.netfliz.admin.entity.MovieGenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieGenreRepository extends JpaRepository<MovieGenreEntity, Long> {
}
