package com.pivovarit.domain.rental;

import com.pivovarit.domain.rental.api.MovieId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class MovieDescriptionsUpdater {

    private static final Logger log = LoggerFactory.getLogger(MovieDescriptionsUpdater.class);

    public void onDescriptionUpdated(MovieId id, String description) {
        log.info("movie description updated: {}:{}", id.id(), description);
    }
}
