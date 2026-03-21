CREATE TABLE stage (
   id              VARCHAR(50)  NOT NULL,
   _competition    VARCHAR(100) NOT NULL REFERENCES competition(id),
   name            VARCHAR(100) NOT NULL,
   ordering        INT NOT NULL,
   PRIMARY KEY (id, _competition)
);

CREATE TABLE event (
    id                    SERIAL PRIMARY KEY,
    season                INT NOT NULL,
    _status               VARCHAR(20) NOT NULL REFERENCES event_status(code),
    event_date            DATE NOT NULL,
    event_time            TIME,
    _sport                INT REFERENCES sport(id),
    _home_team            INT REFERENCES team(id),
    _away_team            INT REFERENCES team(id),
    _venue                INT REFERENCES venue(id),
    _competition          VARCHAR(100) REFERENCES competition(id),
    _stage_id             VARCHAR(50),
    _stage_competition    VARCHAR(100),
    group_name            VARCHAR(50),
    home_stage_position   INT,
    away_stage_position   INT,
    attendance            INT,
    description           TEXT,
    FOREIGN KEY (_stage_id, _stage_competition)
       REFERENCES stage(id, _competition)
);

CREATE TABLE event_result (
    _event        INT PRIMARY KEY REFERENCES event(id),
    home_goals    INT NOT NULL DEFAULT 0,
    away_goals    INT NOT NULL DEFAULT 0,
    _winner_team  INT REFERENCES team(id),
    message       VARCHAR(255),
    notes         TEXT
);

CREATE TABLE goal (
    id            SERIAL PRIMARY KEY,
    _event        INT NOT NULL REFERENCES event(id),
    _team         INT NOT NULL REFERENCES team(id),
    player_name   VARCHAR(100),
    minute        INT,
    is_own_goal   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE card (
    id            SERIAL PRIMARY KEY,
    _event        INT NOT NULL REFERENCES event(id),
    _team         INT NOT NULL REFERENCES team(id),
    _card_type    VARCHAR(20) NOT NULL REFERENCES card_type(code),
    player_name   VARCHAR(100),
    minute        INT
);

CREATE TABLE score_by_period (
    id            SERIAL PRIMARY KEY,
    _event        INT NOT NULL REFERENCES event(id),
    _period       VARCHAR(20) NOT NULL REFERENCES period_type(code),
    home_goals    INT NOT NULL DEFAULT 0,
    away_goals    INT NOT NULL DEFAULT 0,
    CONSTRAINT uq_score_by_period_event_period UNIQUE (_event, _period)
);

CREATE INDEX idx_event_date ON event(event_date);
CREATE INDEX idx_event_status ON event(_status);
CREATE INDEX idx_event_sport ON event(_sport);
CREATE INDEX idx_event_competition ON event(_competition);