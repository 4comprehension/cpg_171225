package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.jpa.PersistedMovie;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("prod")
public interface MovieRepository extends JpaRepository<PersistedMovie, Long> {
    List<PersistedMovie> findByTitle(String title);

    List<PersistedMovie> findByType(String type);
}
