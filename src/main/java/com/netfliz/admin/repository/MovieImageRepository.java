package com.netfliz.admin.repository;

import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.constant.enums.MovieObjectType;
import com.netfliz.admin.entity.MovieImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface MovieImageRepository extends JpaRepository<MovieImageEntity, Long> {

    @Query("SELECT e FROM MovieImageEntity e WHERE e.objectId IN :objectIds AND e.objectType = :objectType")
    List<MovieImageEntity> findByObjectIdsAndObjectType(Collection<Long> objectIds, MovieObjectType objectType);

    @Query("SELECT e FROM MovieImageEntity e WHERE e.objectId = :objectId AND e.objectType = :objectType")
    List<MovieImageEntity> findByObjectIdAndObjectType(Long objectId, MovieObjectType objectType);

    @Query("SELECT e FROM MovieImageEntity e WHERE e.objectId = :objectId AND e.objectType = :objectType AND e.imageType IN :imageTypes")
    List<MovieImageEntity> findByObjectIdAndObjectTypeAndImageTypeIn(Long objectId, MovieObjectType objectType,
                                                                     Collection<MovieImageType> imageTypes);

    void deleteAllByObjectIdAndObjectType(Long objectId, MovieObjectType objectType);

    void deleteAllByObjectIdAndObjectTypeAndImageTypeIn(Long objectId, MovieObjectType objectType, Collection<MovieImageType> imageTypes);
}
