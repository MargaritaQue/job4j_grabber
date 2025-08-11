CREATE TABLE post (
    id serial primary key,
    name VARCHAR(255),
    text TEXT,
    link TEXT UNIQUE,
    created TIMESTAMP WITHOUT TIME ZONE
);