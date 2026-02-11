package com.netfliz.admin.controller.graphql;

import com.netfliz.admin.dto.MovieEpisodeDto;
import com.netfliz.admin.dto.request.MovieEpisodeFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.dto.response.SuggestEpisodeResponse;
import com.netfliz.admin.service.MovieEpisodeService;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class MovieEpisodeGraphql {
    private final MovieEpisodeService movieEpisodeService;

    @QueryMapping
    public PageResponse<MovieEpisodeDto> getMovieEpisodes(@Argument Long movieId, @Argument Integer page,
            @Argument Integer pageSize) {
        MovieEpisodeFilterRequest request = new MovieEpisodeFilterRequest();
        request.setId(movieId);
        request.setPage(page);
        request.setPageSize(pageSize);
        return movieEpisodeService.getMovieEpisodes(request);
    }

    @QueryMapping
    public SuggestEpisodeResponse suggestEpisodeNumber(@Argument Long movieId) {
        return movieEpisodeService.suggestEpisodeNumber(movieId);
    }

    @MutationMapping
    public MovieEpisodeDto updateMovieEpisode(@Argument Long movieId, @Argument MovieEpisodeDto movieEpisode) {
        return movieEpisodeService.updateMovieEpisode(movieId, movieEpisode);
    }

    @MutationMapping
    public MovieEpisodeDto deleteMovieEpisode(@Argument Long movieId, @Argument Long movieEpisodeId) {
        return movieEpisodeService.deleteMovieEpisode(movieId, movieEpisodeId);
    }
}
