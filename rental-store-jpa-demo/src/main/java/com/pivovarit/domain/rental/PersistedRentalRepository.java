package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.jpa.PersistedRental;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Profile("prod")
public interface PersistedRentalRepository
  extends JpaRepository<PersistedRental, Long> {

    List<PersistedRental> findByMovieId(long movieId);

    boolean existsByMovieIdAndRenter(long movieId, String renter);
}
