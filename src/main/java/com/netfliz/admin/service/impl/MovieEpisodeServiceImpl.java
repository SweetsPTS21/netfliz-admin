package com.netfliz.admin.service.impl;

import com.netfliz.admin.constant.enums.MovieAssetType;
import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.constant.enums.MovieObjectType;
import com.netfliz.admin.dto.MovieEpisodeDto;
import com.netfliz.admin.dto.request.MovieEpisodeFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.dto.response.SuggestEpisodeResponse;
import com.netfliz.admin.entity.MovieAssetEntity;
import com.netfliz.admin.entity.MovieEpisodeEntity;
import com.netfliz.admin.entity.MovieImageEntity;
import com.netfliz.admin.mapper.MovieAssetMapper;
import com.netfliz.admin.mapper.MovieEpisodeMapper;
import com.netfliz.admin.mapper.MovieImageMapper;
import com.netfliz.admin.repository.MovieAssetRepository;
import com.netfliz.admin.repository.MovieEpisodeRepository;
import com.netfliz.admin.repository.MovieImageRepository;
import com.netfliz.admin.service.MovieEpisodeService;
import com.netfliz.admin.validator.MovieEpisodeValidator;
import com.netfliz.admin.validator.MovieValidator;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieEpisodeServiceImpl implements MovieEpisodeService {
        private final MovieEpisodeRepository movieEpisodeRepository;
        private final MovieAssetRepository movieAssetRepository;
        private final MovieAssetMapper movieAssetMapper;
        private final MovieEpisodeMapper movieEpisodeMapper;
        private final MovieEpisodeValidator movieEpisodeValidator;
        private final MovieValidator movieValidator;
        private final MovieImageRepository movieImageRepository;
        private final MovieImageMapper movieImageMapper;

        @Override
        public PageResponse<MovieEpisodeDto> getMovieEpisodes(@Valid MovieEpisodeFilterRequest request) {
                movieValidator.validateSeriesMovie(request.getId());

                Pageable pageable = PageRequest.of(request.getPage() - 1, request.getPageSize());
                Page<MovieEpisodeEntity> movieEpisodesPage = movieEpisodeRepository.findByMovieId(request.getId(),
                                pageable);
                List<Long> episodeIds = movieEpisodesPage.getContent().stream().map(MovieEpisodeEntity::getId).toList();

                Map<Long, List<MovieAssetEntity>> mapAsset = movieAssetRepository
                                .findByObjectIds(episodeIds, MovieObjectType.EPISODE)
                                .stream()
                                .collect(Collectors.groupingBy(MovieAssetEntity::getObjectId));
                Map<Long, List<MovieImageEntity>> mapPoster = movieImageRepository
                                .findByObjectIdsAndObjectType(episodeIds, MovieObjectType.EPISODE)
                                .stream()
                                .collect(Collectors.groupingBy(MovieImageEntity::getObjectId));

                List<MovieEpisodeDto> movieEpisodes = movieEpisodesPage.getContent().stream()
                                .map(movieEpisodeEntity -> {
                                        List<MovieAssetEntity> assets = mapAsset
                                                        .getOrDefault(movieEpisodeEntity.getId(), new ArrayList<>());
                                        List<MovieImageEntity> posters = mapPoster
                                                        .getOrDefault(movieEpisodeEntity.getId(), new ArrayList<>());

                                        return movieEpisodeMapper.mapFromEntity(movieEpisodeEntity, assets, posters);
                                }).toList();

                return PageResponse.<MovieEpisodeDto>builder()
                                .page(movieEpisodesPage.getNumber() + 1)
                                .pageSize(movieEpisodesPage.getSize())
                                .total(movieEpisodesPage.getTotalElements())
                                .totalPages(movieEpisodesPage.getTotalPages())
                                .items(movieEpisodes)
                                .build();

        }

        @Override
        @Transactional
        public MovieEpisodeDto updateMovieEpisode(Long movieId, @Valid MovieEpisodeDto movieEpisode) {
                movieValidator.validateSeriesMovie(movieId);
                movieEpisodeValidator.validateEpisode(movieId, movieEpisode);

                // Lưu episode
                MovieEpisodeEntity movieEpisodeEntity = movieEpisodeMapper.mapToEntity(movieId, movieEpisode);
                movieEpisodeRepository.save(movieEpisodeEntity);

                // Xóa toàn bộ poster/assets cũ
                movieImageRepository.deleteAllByObjectIdAndObjectTypeAndImageTypeIn(
                                movieEpisodeEntity.getId(),
                                MovieObjectType.EPISODE,
                                List.of(MovieImageType.POSTER));
                movieAssetRepository.deleteAllByObjectId(
                                movieEpisodeEntity.getId(),
                                MovieObjectType.EPISODE,
                                List.of(MovieAssetType.VIDEO, MovieAssetType.SUBTITLE));

                // Lưu posters
                List<MovieImageEntity> movieImageEntities = new ArrayList<>();
                if (!CollectionUtils.isEmpty(movieEpisode.getPosters())) {
                        movieImageEntities.addAll(movieImageMapper.mapToEntities(
                                        movieEpisode.getPosters(),
                                        movieEpisodeEntity.getId(),
                                        MovieObjectType.EPISODE));
                        movieImageRepository.saveAll(movieImageEntities);
                }

                // Lưu assets
                List<MovieAssetEntity> movieAssetEntities = new ArrayList<>();
                if (!CollectionUtils.isEmpty(movieEpisode.getAssets())) {
                        movieAssetEntities.addAll(movieAssetMapper.mapToEntities(
                                        movieEpisodeEntity.getId(),
                                        MovieObjectType.EPISODE,
                                        movieEpisode.getAssets()));
                        movieAssetRepository.saveAll(movieAssetEntities);
                }

                return movieEpisodeMapper.mapFromEntity(
                                movieEpisodeEntity,
                                movieAssetEntities,
                                movieImageEntities);
        }

        /**
         * Suggest episode number and order
         *
         * @param movieId movieId
         * @return SuggestEpisodeNumber
         */
        @Override
        public SuggestEpisodeResponse suggestEpisodeNumber(Long movieId) {
                Integer maxEpisodeNumber = movieEpisodeRepository.findMaxEpisodeNumberByMovieId(movieId).orElse(0);
                Integer maxEpisodeOrder = movieEpisodeRepository.findMaxEpisodeOrderByMovieId(movieId).orElse(0);

                return SuggestEpisodeResponse.builder()
                                .episodeNumber(maxEpisodeNumber + 1)
                                .episodeOrder(maxEpisodeOrder + 1)
                                .build();
        }

        @Override
        @Transactional
        public MovieEpisodeDto deleteMovieEpisode(Long movieId, Long movieEpisodeId) {
                movieValidator.validateSeriesMovie(movieId);

                MovieEpisodeEntity episodeEntity = movieEpisodeRepository.findById(movieEpisodeId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Không tìm thấy tập phim với id: " + movieEpisodeId));

                List<MovieAssetEntity> assets = movieAssetRepository.findByObjectIds(
                                List.of(movieEpisodeId), MovieObjectType.EPISODE);
                List<MovieImageEntity> images = movieImageRepository.findByObjectIdsAndObjectType(
                                List.of(movieEpisodeId), MovieObjectType.EPISODE);

                MovieEpisodeDto result = movieEpisodeMapper.mapFromEntity(episodeEntity, assets, images);

                movieAssetRepository.deleteAllByObjectId(movieEpisodeId, MovieObjectType.EPISODE,
                                List.of(MovieAssetType.VIDEO, MovieAssetType.SUBTITLE));
                movieImageRepository.deleteAllByObjectIdAndObjectTypeAndImageTypeIn(movieEpisodeId,
                                MovieObjectType.EPISODE, List.of(MovieImageType.POSTER));
                movieEpisodeRepository.delete(episodeEntity);

                return result;
        }
}
