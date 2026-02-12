package com.netfliz.admin.service.impl;

import com.netfliz.admin.constant.Constants;
import com.netfliz.admin.constant.enums.*;
import com.netfliz.admin.dto.MovieAssetDto;
import com.netfliz.admin.dto.MovieDto;
import com.netfliz.admin.dto.MovieImageDto;
import com.netfliz.admin.dto.MovieMetadataDto;
import com.netfliz.admin.dto.request.MovieFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.entity.MovieAssetEntity;
import com.netfliz.admin.entity.MovieEntity;
import com.netfliz.admin.entity.MovieImageEntity;
import com.netfliz.admin.exception.BadRequestException;
import com.netfliz.admin.exception.NotFoundException;
import com.netfliz.admin.mapper.MovieAssetMapper;
import com.netfliz.admin.mapper.MovieImageMapper;
import com.netfliz.admin.mapper.MovieMapper;
import com.netfliz.admin.repository.CustomMovieRepository;
import com.netfliz.admin.repository.MovieAssetRepository;
import com.netfliz.admin.repository.MovieImageRepository;
import com.netfliz.admin.repository.MovieRepository;
import com.netfliz.admin.service.MovieGenreService;
import com.netfliz.admin.service.MovieService;
import com.netfliz.admin.validator.MovieAssetValidator;
import com.netfliz.admin.validator.MovieValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final MovieImageRepository movieImageRepository;
    private final MovieAssetRepository movieAssetRepository;
    private final MovieMapper movieMapper;
    private final MovieValidator movieValidator;
    private final MovieImageMapper movieImageMapper;
    private final MovieAssetMapper movieAssetMapper;
    private final MovieAssetValidator movieAssetValidator;
    private final CustomMovieRepository customMovieRepository;
    private final MovieGenreService movieGenreService;

    private static String buildSlug(String value) {
        return value.toLowerCase().replace(" ", "-");
    }

    @Override
    public PageResponse<MovieDto> getMoviesByFilter(MovieFilterRequest request) {
        Page<MovieEntity> resultPage = customMovieRepository.findByFilter(request);
        List<MovieDto> movies = movieMapper.mapToDtoList(resultPage.getContent());

        // map to movies
        mapMovieImageAndAsset(movies, movies.stream().map(MovieDto::getId).collect(Collectors.toSet()));

        return PageResponse.<MovieDto>builder()
                .page(resultPage.getNumber())
                .pageSize(resultPage.getSize())
                .total(resultPage.getTotalElements())
                .totalPages(resultPage.getTotalPages())
                .items(movies)
                .build();
    }

    @Override
    public MovieDto getMovieById(Long id) {
        if (Objects.isNull(id)) {
            throw new BadRequestException("Movie id is required");
        }

        MovieEntity entity = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id: " + id));
        MovieDto movie = movieMapper.buildFromEntity(entity);
        mapMovieImageAndAsset(List.of(movie), List.of(id));

        return movie;
    }

    @Override
    @Transactional
    public MovieDto createMovie(MovieDto request) {
        MovieEntity movieEntity = movieMapper.mapToEntity(request);
        movieRepository.save(movieEntity);

        // save movie image
        List<MovieImageDto> movieImages = request.getImages();
        if (!CollectionUtils.isEmpty(movieImages)) {
            movieValidator.validateMovieImage(movieImages);
            movieImageRepository.saveAll(movieImageMapper.mapToEntities(
                    movieImages, movieEntity.getId(), MovieObjectType.MOVIE));
        }

        // save movie assets
        List<MovieAssetDto> movieAssets = request.getAssets();
        if (!CollectionUtils.isEmpty(movieAssets)) {
            movieAssetValidator.validateAssets(movieAssets);
            movieAssetRepository.saveAll(movieAssetMapper.mapToEntities(
                    movieEntity.getId(), MovieObjectType.MOVIE, movieAssets));
        }

        return movieMapper.buildFromEntity(movieEntity);
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Long id, MovieDto request) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found with id: " + id);
        }

        MovieEntity movieEntity = movieMapper.mapToEntity(request);
        movieEntity.setId(id);
        movieRepository.save(movieEntity);

        // update movie image
        updateMovieImage(movieEntity.getId(), request.getImages());

        // update movie assets
        updateMovieAssets(movieEntity.getId(), request.getAssets());

        return movieMapper.buildFromEntity(movieEntity);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found with id: " + id);
        }

        movieRepository.deleteById(id);
        movieImageRepository.deleteAllByObjectIdAndObjectType(id, MovieObjectType.MOVIE);
        movieAssetRepository.deleteAllByObjectIdAndObjectType(id, MovieObjectType.MOVIE);
    }

    @Override
    @Transactional
    public Integer bulkMovie(List<MovieDto> movies) {
        if (CollectionUtils.isEmpty(movies)) {
            return 0;
        }

        List<MovieEntity> entityList = movies.stream().map(movieMapper::mapToEntity).toList();

        // Insert in batches of 200
        int batchSize = 200;
        int totalInserted = 0;

        for (int i = 0; i < entityList.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, entityList.size());
            List<MovieEntity> batch = entityList.subList(i, endIndex);
            movieRepository.saveAll(batch);
            totalInserted += batch.size();
        }

        return totalInserted;
    }

    @Override
    public MovieMetadataDto getMovieMetadata() {
        var genreList = Optional.ofNullable(movieGenreService.getAllMovieGenres()).orElse(new ArrayList<>());
        var genres = genreList.stream()
                .map(genre -> buildMetadata(genre.getName(), genre.getTitle()))
                .toList();
        var countries = Arrays.stream(MovieCountry.values())
                .map(country -> buildMetadata(country.getValue(), country.getDescription()))
                .toList();
        var languages = Arrays.stream(MovieLanguage.values())
                .map(language -> buildMetadata(language.getValue(), language.getDescription()))
                .toList();
        var rated = Arrays.stream(MovieRated.values())
                .map(rate -> buildMetadata(rate.getValue(), rate.getDescription()))
                .toList();
        var types = Arrays.stream(MovieType.values())
                .map(type -> buildMetadata(type.getValue(), type.getDescription()))
                .toList();
        var years = Constants.DEFAULT_YEARS.stream()
                .map(year -> buildMetadata(String.valueOf(year), String.valueOf(year)))
                .toList();

        return MovieMetadataDto.builder()
                .genres(genres)
                .countries(countries)
                .languages(languages)
                .rated(rated)
                .types(types)
                .years(years)
                .build();
    }

    private void mapMovieImageAndAsset(List<MovieDto> movies, Collection<Long> movieIds) {
        Map<Long, List<MovieImageEntity>> mapImage = movieImageRepository
                .findByObjectIdsAndObjectType(movieIds, MovieObjectType.MOVIE)
                .stream()
                .collect(Collectors.groupingBy(MovieImageEntity::getObjectId));

        Map<Long, List<MovieAssetEntity>> mapAsset = movieAssetRepository
                .findByObjectIds(movieIds, MovieObjectType.MOVIE)
                .stream()
                .collect(Collectors.groupingBy(MovieAssetEntity::getObjectId));

        movies.forEach(movie -> {
            Optional.ofNullable(mapImage.get(movie.getId()))
                    .ifPresent(movieImages -> movie
                            .setImages(movieImages.stream().map(MovieImageDto::buildFromEntity).toList()));
            Optional.ofNullable(mapAsset.get(movie.getId()))
                    .ifPresent(movieAssets -> movie
                            .setAssets(movieAssets.stream().map(MovieAssetDto::buildFromEntity).toList()));
        });
    }

    public void updateMovieImage(Long movieId, List<MovieImageDto> movieImages) {
        if (CollectionUtils.isEmpty(movieImages)) {
            return;
        }

        // validate image
        movieValidator.validateMovieImage(movieImages);

        // update movie image
        List<MovieImageDto> updatedImages = getUpdateImage(movieImages, movieId);

        // find and delete old image which has type in movieImages
        List<Integer> imageTypes = updatedImages.stream().map(MovieImageDto::getType).toList();
        List<Long> oldImageIds = movieImageRepository
                .findByObjectIdAndObjectTypeAndImageTypeIn(
                        movieId,
                        MovieObjectType.MOVIE,
                        imageTypes.stream().map(MovieImageType::fromId).toList())
                .stream()
                .map(MovieImageEntity::getId)
                .toList();

        if (!CollectionUtils.isEmpty(oldImageIds)) {
            movieImageRepository.deleteAllById(oldImageIds);
        }

        // save new image
        if (!CollectionUtils.isEmpty(movieImages)) {
            movieImageRepository.saveAll(movieImageMapper.mapToEntities(
                    movieImages, movieId, MovieObjectType.MOVIE));
        }
    }

    public void updateMovieAssets(Long movieId, List<MovieAssetDto> movieAssets) {
        if (CollectionUtils.isEmpty(movieAssets)) {
            return;
        }
        movieAssetValidator.validateAssets(movieAssets);
        List<MovieAssetDto> updatedAssets = getUpdateAssets(movieId, movieAssets);

        // Xóa tất cả assets
        movieAssetRepository.deleteAllByObjectId(
                movieId,
                MovieObjectType.MOVIE,
                List.of(MovieAssetType.VIDEO, MovieAssetType.TRAILER, MovieAssetType.SUBTITLE));

        if (!CollectionUtils.isEmpty(updatedAssets)) {
            movieAssetRepository.saveAll(movieAssetMapper.mapToEntities(movieId, MovieObjectType.MOVIE, updatedAssets));
        }
    }

    /**
     * Lấy ra các image cần update (chưa tồn tại trong db)
     */
    public List<MovieImageDto> getUpdateImage(List<MovieImageDto> images, Long movieId) {
        Map<Long, List<MovieImageEntity>> map = movieImageRepository
                .findByObjectIdAndObjectType(movieId, MovieObjectType.MOVIE)
                .stream()
                .collect(Collectors.groupingBy(MovieImageEntity::getFileId));

        List<MovieImageDto> updated = new ArrayList<>();
        images.forEach(image -> {
            if (map.containsKey(image.getId())) {
                return;
            }
            updated.add(image);
        });

        return updated;
    }

    /**
     * Lấy ra các asset cần update (chưa tồn tại trong db)
     */
    public List<MovieAssetDto> getUpdateAssets(Long movieId, List<MovieAssetDto> assets) {
        Map<Long, List<MovieAssetEntity>> map = movieAssetRepository.findByObjectId(movieId, MovieObjectType.MOVIE)
                .stream()
                .collect(Collectors.groupingBy(MovieAssetEntity::getFileId));

        List<MovieAssetDto> updated = new ArrayList<>();
        assets.forEach(asset -> {
            if (map.containsKey(asset.getFileId())) {
                return;
            }
            updated.add(asset);
        });

        return updated;
    }

    private MovieMetadataDto.Metadata buildMetadata(String value, String description) {
        return MovieMetadataDto.Metadata.builder()
                .value(value)
                .description(description)
                .slug(buildSlug(value))
                .build();
    }
}
