CREATE TABLE movie_descriptions (
    movie_id BIGINT PRIMARY KEY,
    description TEXT NOT NULL
);

CREATE INDEX idx_movie_id ON movie_descriptions(movie_id);
