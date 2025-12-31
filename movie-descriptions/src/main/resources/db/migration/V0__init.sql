CREATE TABLE movie_descriptions
(
    id   BIGINT PRIMARY KEY,
    description TEXT NOT NULL
);

INSERT INTO movie_descriptions (id, description) VALUES (0, 'movie about java');
INSERT INTO movie_descriptions (id, description) VALUES (1, 'movie about something else');
INSERT INTO movie_descriptions (id, description) VALUES (2, 'movie about movie with id == 2');