package org.example.dto;

import java.util.List;

public record MatchesListResponse(
        List<MatchSummaryResponse> matches,
        int currentPage,
        int totalPages
) {
}