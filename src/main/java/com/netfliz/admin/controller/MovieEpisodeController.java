package com.netfliz.admin.controller;

import com.netfliz.admin.dto.MovieEpisodeDto;
import com.netfliz.admin.dto.request.MovieEpisodeFilterRequest;
import com.netfliz.admin.dto.response.PageResponse;
import com.netfliz.admin.dto.response.SuggestEpisodeResponse;
import com.netfliz.admin.service.MovieEpisodeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/episodes")
@AllArgsConstructor
public class MovieEpisodeController {
    private final MovieEpisodeService movieEpisodeService;

    @PostMapping("")
    public ResponseEntity<PageResponse<MovieEpisodeDto>> getMovieEpisodes(@RequestBody @Valid MovieEpisodeFilterRequest request) {
        return ResponseEntity.ok(movieEpisodeService.getMovieEpisodes(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieEpisodeDto> updateMovieEpisode(@PathVariable Long id, @RequestBody @Valid MovieEpisodeDto movieEpisode) {
        return ResponseEntity.ok(movieEpisodeService.updateMovieEpisode(id, movieEpisode));
    }

    @GetMapping("/suggest")
    public ResponseEntity<SuggestEpisodeResponse> suggestEpisodeNumber(@RequestParam Long id) {
        return ResponseEntity.ok(movieEpisodeService.suggestEpisodeNumber(id));
    }
}
