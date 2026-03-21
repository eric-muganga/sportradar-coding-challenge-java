CREATE TABLE venue (
    id           SERIAL PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    city         VARCHAR(100),
    _country     VARCHAR(3) REFERENCES country(code),
    capacity     INT,
    address      VARCHAR(255),
    surface      VARCHAR(50)
);

CREATE TABLE team (
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    official_name  VARCHAR(150),
    abbreviation   VARCHAR(10),
    slug    VARCHAR(200) NOT NULL UNIQUE,
    _country       VARCHAR(3) REFERENCES country(code),
    founded_year   INT,
    _home_venue    INT REFERENCES venue(id)
);