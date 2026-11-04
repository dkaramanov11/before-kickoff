package com.beforekickoff.backend.repository.specification;

import com.beforekickoff.backend.model.FootballMatch;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public final class FootballMatchSpecifications {

    private FootballMatchSpecifications() {
    }

    public static Specification<FootballMatch> hasTeam(Long teamId) {
        return (root, query, cb) -> {
            if (teamId == null) {
                return cb.conjunction();
            }

            return cb.or(
                    cb.equal(root.get("homeTeam").get("id"), teamId),
                    cb.equal(root.get("awayTeam").get("id"), teamId)
            );
        };
    }

    public static Specification<FootballMatch> hasStatus(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), status);
        };
    }

    public static Specification<FootballMatch> kickoffFrom(Instant from) {
        return (root, query, cb) -> {
            if (from == null) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(
                    root.<Instant>get("kickoffAt"),
                    from
            );
        };
    }

    public static Specification<FootballMatch> kickoffBefore(Instant toExclusive) {
        return (root, query, cb) -> {
            if (toExclusive == null) {
                return cb.conjunction();
            }

            return cb.lessThan(
                    root.<Instant>get("kickoffAt"),
                    toExclusive
            );
        };
    }
}