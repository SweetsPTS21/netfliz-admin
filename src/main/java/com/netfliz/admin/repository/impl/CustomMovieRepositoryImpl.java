package com.netfliz.admin.repository.impl;

import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.entity.MovieEntity;
import com.netfliz.admin.repository.CustomMovieRepository;
import com.netfliz.admin.util.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CustomMovieRepositoryImpl implements CustomMovieRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<MovieEntity> rowMapper = (rs, rowNum) -> {
        MovieEntity m = new MovieEntity();
        m.setId(rs.getLong("id"));
        m.setTitle(rs.getString("title"));
        m.setGenre(JsonUtils.parse(rs.getString("genre")));
        m.setYear(rs.getInt("year"));
        m.setRated(rs.getString("rated"));
        m.setReleased(rs.getString("released"));
        m.setRuntime(rs.getString("runtime"));
        m.setDirector(rs.getString("director"));
        m.setWriter(rs.getString("writer"));
        m.setActors(rs.getString("actors"));
        m.setPlot(rs.getString("plot"));
        m.setLanguages(JsonUtils.parse(rs.getString("languages")));
        m.setCountries(JsonUtils.parse(rs.getString("countries")));
        m.setAwards(rs.getString("awards"));
        m.setMetaScore(rs.getLong("meta_score"));
        m.setImdbRating(rs.getString("imdb_rating"));
        m.setType(rs.getString("type"));
        m.setCategories(JsonUtils.parse(rs.getString("categories")));

        return m;
    };

    @Override
    public Page<MovieEntity> findByFilter(MovieFilterRequest request) {
        StringBuilder whereClause = new StringBuilder(" WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (!CollectionUtils.isEmpty(request.getGenres())) {
            whereClause.append(" AND jsonb_exists_any(genre, :genres)");
            String[] genres = request.getGenres().toArray(new String[0]);
            params.addValue("genres", new SqlParameterValue(Types.ARRAY, "text", genres));
        }

        if (!CollectionUtils.isEmpty(request.getCountries())) {
            whereClause.append(" AND country IN (:countries)");
            params.addValue("countries", request.getCountries());
        }

        if (!CollectionUtils.isEmpty(request.getLanguages())) {
            whereClause.append(" AND languages IN (:languages)");
            params.addValue("languages", request.getLanguages());
        }

        if (Strings.isNotBlank(request.getYear())) {
            whereClause.append(" AND year = :year");
            params.addValue("year", Integer.parseInt(request.getYear()));
        }

        if (Strings.isNotBlank(request.getRating())) {
            whereClause.append(" AND imdb_rating >= :imdb_rating");
            params.addValue("imdb_rating", request.getRating());
        }

        if (Strings.isNotBlank(request.getRated())) {
            whereClause.append(" AND rated = :rated");
            params.addValue("rated", request.getRated());
        }

        if (Strings.isNotBlank(request.getType())) {
            whereClause.append(" AND type = :type");
            params.addValue("type", request.getType());
        }

        if (Strings.isNotBlank(request.getTitle())) {
            whereClause.append(" AND title ILIKE :title");
            params.addValue("title", "%" + request.getTitle() + "%");
        }

        // Count
        String countSql = "SELECT COUNT(*) FROM movies" + whereClause;
        Long total = Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(countSql, params, Long.class)).orElse(0L);

        whereClause.append(" ORDER BY ");
        if (Strings.isNotBlank(request.getSort())) {
            switch (request.getSort().toLowerCase()) {
                case "year":
                case "imdb_rating":
                case "released":
                case "updated_at":
                    whereClause.append(request.getSort().toLowerCase());
                    break;
                default:
                    whereClause.append("updated_at");
            }
            whereClause.append(" DESC");
        } else {
            whereClause.append("updated_at DESC");
        }

        // Add pagination
        whereClause.append(" LIMIT :limit OFFSET :offset");
        params.addValue("limit", request.getPageSize());
        params.addValue("offset", (long) request.getPage() * request.getPageSize());

        // Build final query
        String searchSql = "SELECT * FROM movies" + whereClause;
        List<MovieEntity> result = namedParameterJdbcTemplate.query(searchSql, params, rowMapper);

        return new PageImpl<>(result, PageRequest.of(request.getPage(), request.getPageSize()), total);
    }

}
