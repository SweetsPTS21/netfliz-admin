package com.netfliz.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netfliz.admin.constant.enums.MovieImageType;
import com.netfliz.admin.constant.enums.MovieObjectType;
import com.netfliz.admin.entity.converter.MovieImageTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Table(name = "movie_images")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "format")
    private String format;

    @Column(name = "image_type")
    @Convert(converter = MovieImageTypeConverter.class)
    private MovieImageType imageType;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "object_id")
    private Long objectId;

    @Column(name = "object_type")
    private MovieObjectType objectType;

    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "created_at", insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date updatedAt;
}
