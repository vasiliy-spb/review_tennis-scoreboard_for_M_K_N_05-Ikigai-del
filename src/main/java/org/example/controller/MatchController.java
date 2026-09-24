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

    // TODO: Зависимость `MatchService` создаётся напрямую в месте объявления.
        // Это нарушает Принцип инверсии зависимостей (DIP).
        // Класс не должен сам решать, какую конкретно реализацию использовать.
        // Он должен получать её извне через конструктор.

    // TODO: Контроллер берёт на себя лишнюю ответственность —
        // занимается бизнес логикой (расчёт totalPages) и
        // преобразованием JPA Entity в DTO (List<Match> —> List<MatchSummaryResponse>),
        // хотя его задача — только принимать HTTP-запросы и делегировать их обработку.
        // Это нарушает принцип единственной ответственности (SRP)
        // и делает код контроллера более сложным.
        // Контроллер должен быть "тонким", делегирующим всю бизнес-логику сервисному слою.
        // (см. файл "fat-controller.md" в этом же пакете)

    // Все повторяющиеся или важные строковые литералы лучше выносить в `private static final` константы с понятными именами.
        // Именованная константа делает код более семантически понятным.

    // TODO: Контроллер работает с JPA Entity (List<Match>) и доменной моделью (MatchScore).
        // Это нарушает границы между слоями приложения и Принцип разделения ответственности
        // (см. файл "separation-of-concerns-principle.md" в этом же пакете)
        // Контроллер не должен работать с JPA Entity и доменными моделями.
        // Вместо этого он должен "общаться" с другими слоями через DTO.

    // TODO: Набор обрабатываемых исключений неполный (только MatchNotFoundException).
        // Стоит добавить обработку и других возможных исключений, в том числе общий обработчик
        // для всех непредвиденных ошибок (Exception), чтобы клиент никогда не получал стектрейс.
        // И лучше вынести всю обработку в отдельный класс.

    private final MatchService matchService = new MatchService();

    // Код, не предназначенный для работы приложения стоит удалять перед коммитом.
    @GetMapping("/")
    public String test() {
        return "Spring MVC работает";
    }

    @PostMapping
    public ResponseEntity<CreateMatchResponse> createMatch(
            @RequestBody CreateMatchRequest request) { // Если параметр один и умещается в строку, его можно не переносить
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

        // Размер страницы по умолчанию можно вынести в константу
        int pageSize = 10;

        // Логику преобразования JPA Entity в DTO лучше запускать из сервиса.
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
        return ResponseEntity.notFound().build(); // Можно добавить информативное сообщение
    }
}