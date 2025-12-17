package com.pivovarit.rental.listener;

import com.pivovarit.rental.model.MovieId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieDescriptionsUpdater {

    private static final Logger log = LoggerFactory.getLogger(MovieDescriptionsUpdater.class);

    public void onDescriptionUpdated(MovieId id, String description) {
        log.info("movie description updated: {}:{}", id.id(), description);
    }
}
