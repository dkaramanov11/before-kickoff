package com.beforekickoff.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_api_id", unique = true)
    private Long externalApiId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "short_name", length = 100)
    private String shortName;

    @Column(name = "crest_url", columnDefinition = "TEXT")
    private String crestUrl;

    public Team(Long externalApiId, String name,
                String shortName, String crestUrl) {
        this.externalApiId = externalApiId;
        this.name = name;
        this.shortName = shortName;
        this.crestUrl = crestUrl;
    }

    public void updateDetails(String name, String shortName, String crestUrl) {
        this.name = name;
        this.shortName = shortName;
        this.crestUrl = crestUrl;
    }
}