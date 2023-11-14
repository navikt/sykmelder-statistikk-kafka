ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO "sykmelder-statistikk-next";

CREATE TABLE test_table
(
    id   VARCHAR primary key not null,
    text VARCHAR             not null
);
