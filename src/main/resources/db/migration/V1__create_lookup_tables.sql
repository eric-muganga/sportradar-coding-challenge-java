CREATE TABLE country (
     code        VARCHAR(3)  PRIMARY KEY,
     name        VARCHAR(100) NOT NULL
);

CREATE TABLE event_status (
    code        VARCHAR(20) PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE card_type (
    code        VARCHAR(20) PRIMARY KEY,
    description VARCHAR(100) NOT NULL
);

CREATE TABLE period_type (
    code        VARCHAR(20) PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    ordering    INT NOT NULL
);

CREATE TABLE sport (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE competition (
    id          VARCHAR(100) PRIMARY KEY,
    name        VARCHAR(100) NOT NULL
);