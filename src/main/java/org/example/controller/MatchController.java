package org.example.controller;

import org.example.dto.*;
import org.example.exception.MatchNotFoundException;
import org.example.model.MatchScore;
import org.example.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchService matchService = new MatchService();

    @GetMapping("/")
    public String test() {
        return "Spring MVC работает";
    }

    @PostMapping
    public ResponseEntity<CreateMatchResponse> createMatch(
            @RequestBody CreateMatchRequest request) {
        UUID uuid = matchService.createMatch(
                request.firstPlayerName(),
                request.secondPlayerName()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new CreateMatchResponse(uuid));
    }

    @PostMapping("/{uuid}/point")
    public ResponseEntity<MatchScoreResponse> addPoint(
            @PathVariable("uuid") UUID uuid,
            @RequestBody PointRequest request) {
        MatchScore matchScore = matchService.addPoint(uuid, request.name());
        return ResponseEntity.ok(MatchScoreMapper.toResponse(matchScore));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<MatchScoreResponse> getMatch(@PathVariable("uuid") UUID uuid) {
        MatchScore matchScore = matchService.getMatch(uuid);
        return ResponseEntity.ok(MatchScoreMapper.toResponse(matchScore));
    }

    @GetMapping
    public ResponseEntity<MatchesListResponse> getMatches(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "player_name", required = false) String playerName) {

        int pageSize = 10;

        List<MatchSummaryResponse> matches = matchService.getFinishedMatches(page, pageSize, playerName)
                .stream()
                .map(MatchScoreMapper::toSummary)
                .toList();

        long totalCount = matchService.countFinishedMatches(playerName);
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        return ResponseEntity.ok(new MatchesListResponse(matches, page, totalPages));
    }

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<Void> handleMatchNotFound() {
        return ResponseEntity.notFound().build();
    }
}