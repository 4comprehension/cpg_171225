CREATE TABLE IF NOT EXISTS movie_descriptions (
    movie_id BIGINT PRIMARY KEY,
    description TEXT NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE
);
