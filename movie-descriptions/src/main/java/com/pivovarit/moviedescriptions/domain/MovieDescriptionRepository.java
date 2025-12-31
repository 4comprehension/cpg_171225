package com.pivovarit.moviedescriptions.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieDescriptionRepository extends JpaRepository<MovieDescriptionEntity, Long> {
}
