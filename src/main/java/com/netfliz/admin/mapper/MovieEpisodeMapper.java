package com.netfliz.admin.mapper;

import com.netfliz.admin.dto.MovieEpisodeDto;
import com.netfliz.admin.entity.MovieAssetEntity;
import com.netfliz.admin.entity.MovieEpisodeEntity;
import com.netfliz.admin.entity.MovieImageEntity;
import com.netfliz.admin.util.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class MovieEpisodeMapper {
    private final MovieAssetMapper movieAssetMapper;
    private final MovieImageMapper movieImageMapper;

    public MovieEpisodeDto mapFromEntity(MovieEpisodeEntity from,
                                         List<MovieAssetEntity> assetEntities,
                                         List<MovieImageEntity> posterEntities) {
        MovieEpisodeDto to = new MovieEpisodeDto();

        to.setId(from.getId());
        to.setName(from.getName());
        to.setEpisodeNumber(from.getEpisodeNumber());
        to.setEpisodeOrder(from.getEpisodeOrder());
        to.setDescription(from.getDescription());
        to.setIsPublished(from.getIsPublished());
        to.setRuntime(from.getRuntime());
        to.setMetadata(JsonUtils.serialize(from.getMetadata()));
        to.setAssets(movieAssetMapper.mapFromEntities(assetEntities));
        to.setPosters(movieImageMapper.mapFromEntities(posterEntities));

        return to;
    }

    public MovieEpisodeEntity mapToEntity(Long movieId, MovieEpisodeDto from) {
        MovieEpisodeEntity to = new MovieEpisodeEntity();

        if (Objects.nonNull(from.getId())) {
            to.setId(from.getId());
        }
        to.setMovieId(movieId);
        to.setName(from.getName());
        to.setEpisodeNumber(from.getEpisodeNumber());
        to.setEpisodeOrder(from.getEpisodeOrder());
        to.setDescription(from.getDescription());
        to.setIsPublished(from.getIsPublished());
        to.setRuntime(from.getRuntime());
        if (Strings.isNotBlank(from.getMetadata())) {
            to.setMetadata(JsonUtils.parse(from.getMetadata()));
        }

        return to;
    }
}
