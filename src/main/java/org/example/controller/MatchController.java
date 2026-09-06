package org.example.controller;

import org.example.dto.CreateMatchRequest;
import org.example.dto.CreateMatchResponse;
import org.example.service.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
