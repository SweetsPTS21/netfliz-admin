package com.netfliz.admin.validator;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.netfliz.admin.dto.MovieAssetDto;

import jakarta.validation.ValidationException;

@Component
public class MovieAssetValidator {
    public void validateAssets(List<MovieAssetDto> assets) {
        if (!CollectionUtils.isEmpty(assets)) {
            assets.forEach(asset -> {
                if (Strings.isBlank(asset.getUrl())) {
                    throw new ValidationException("URL không được để trống");
                }

                if (asset.getAssetType() == null) {
                    throw new ValidationException("Loại asset không được để trống");
                }
            });
        }
    }
}