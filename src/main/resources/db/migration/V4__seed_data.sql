-- ── Lookup data ───────────────────────────────────────────────────────────
INSERT INTO event_status (code, description) VALUES
                                                 ('played',    'Match has been played'),
                                                 ('scheduled', 'Match is scheduled'),
                                                 ('cancelled', 'Match has been cancelled'),
                                                 ('postponed', 'Match has been postponed');

INSERT INTO card_type (code, description) VALUES
                                              ('YELLOW',        'Yellow card'),
                                              ('SECOND_YELLOW', 'Second yellow card'),
                                              ('RED',           'Direct red card');

INSERT INTO period_type (code, description, ordering) VALUES
                                                          ('FIRST_HALF',  'First Half',  1),
                                                          ('SECOND_HALF', 'Second Half', 2),
                                                          ('EXTRA_TIME',  'Extra Time',  3),
                                                          ('PENALTIES',   'Penalties',   4);

INSERT INTO sport (name) VALUES
                             ('Football'),
                             ('Ice Hockey'),
                             ('Basketball'),
                             ('Tennis');

INSERT INTO country (code, name) VALUES
                                     ('ENG', 'England'),
                                     ('KSA', 'Saudi Arabia'),
                                     ('UZB', 'Uzbekistan'),
                                     ('QAT', 'Qatar'),
                                     ('UAE', 'United Arab Emirates'),
                                     ('JPN', 'Japan'),
                                     ('AUT', 'Austria'),
                                     ('IRN', 'Iran');

INSERT INTO competition (id, name) VALUES
                                       ('premier-league',       'Premier League'),
                                       ('afc-champions-league', 'AFC Champions League'),
                                       ('austrian-bundesliga',  'Austrian Bundesliga'),
                                       ('ebel',                 'ICE Hockey League');

INSERT INTO stage (id, _competition, name, ordering) VALUES
                                                         ('ROUND OF 16',  'afc-champions-league', 'Round of 16',   4),
                                                         ('QUARTER_FINAL','afc-champions-league', 'Quarter Final', 5),
                                                         ('SEMI_FINAL',   'afc-champions-league', 'Semi Final',    6),
                                                         ('FINAL',        'afc-champions-league', 'Final',         7);

-- ── Teams ─────────────────────────────────────────────────────────────────
INSERT INTO team (name, official_name, abbreviation, slug, _country) VALUES
                                                                         -- Premier League
                                                                         ('Bournemouth',       'AFC Bournemouth',           'BOU', 'afc-bournemouth',       'ENG'),
                                                                         ('Manchester United', 'Manchester United FC',      'MUN', 'manchester-united',     'ENG'),
                                                                         ('Brighton',          'Brighton & Hove Albion FC', 'BHA', 'brighton-hove-albion',  'ENG'),
                                                                         ('Liverpool',         'Liverpool FC',              'LIV', 'liverpool-fc',          'ENG'),
                                                                         ('Fulham',            'Fulham FC',                 'FUL', 'fulham-fc',             'ENG'),
                                                                         ('Burnley',           'Burnley FC',                'BUR', 'burnley-fc',            'ENG'),
                                                                         ('Everton',           'Everton FC',                'EVE', 'everton-fc',            'ENG'),
                                                                         ('Chelsea',           'Chelsea FC',                'CHE', 'chelsea-fc',            'ENG'),
                                                                         ('Leeds United',      'Leeds United AFC',          'LEE', 'leeds-united',          'ENG'),
                                                                         ('Brentford',         'Brentford FC',              'BRE', 'brentford-fc',          'ENG'),
                                                                         -- AFC Champions League
                                                                         ('Al Shabab',         'Al Shabab FC',              'SHA', 'al-shabab-fc',          'KSA'),
                                                                         ('Nasaf',             'FC Nasaf',                  'NAS', 'fc-nasaf-qarshi',       'UZB'),
                                                                         ('Al Hilal',          'Al Hilal Saudi FC',         'HIL', 'al-hilal-saudi-fc',    'KSA'),
                                                                         ('Shabab Al Ahli',    'SHABAB AL AHLI DUBAI',      'SAH', 'shabab-al-ahli-club',  'UAE'),
                                                                         ('Al Duhail',         'AL DUHAIL SC',              'DUH', 'al-duhail-sc',         'QAT'),
                                                                         ('Al Rayyan',         'AL RAYYAN SC',              'RYN', 'al-rayyan-sc',         'QAT'),
                                                                         ('Al Faisaly',        'Al Faisaly FC',             'FAI', 'al-faisaly-fc',        'KSA'),
                                                                         ('Foolad',            'FOOLAD KHOUZESTAN FC',      'FLD', 'foolad-khuzestan-fc',  'IRN'),
                                                                         ('Urawa Reds',        'Urawa Red Diamonds',        'RED', 'urawa-red-diamonds',   'JPN'),
                                                                         -- Austrian
                                                                         ('Salzburg',          'FC Red Bull Salzburg',      'SAL', 'fc-red-bull-salzburg', 'AUT'),
                                                                         ('Sturm',             'SK Sturm Graz',             'STU', 'sk-sturm-graz',        'AUT'),
                                                                         ('KAC',               'EC Klagenfurter AC',        'KAC', 'kac-klagenfurt',       'AUT'),
                                                                         ('Capitals',          'Vienna Capitals',           'CAP', 'vienna-capitals',      'AUT');

-- ── Events WITHOUT stage columns ─────────────────────────────────────────
-- Premier League Matchweek 31 — Saturday 21 March 2026
-- Source: TNT Sports / Goal.com
-- Friday result: Bournemouth 2-2 Manchester United
-- Saturday fixtures: Brighton vs Liverpool, Fulham vs Burnley,
--                    Everton vs Chelsea, Leeds vs Brentford
INSERT INTO event (season, _status, event_date, event_time, _sport,
                   _home_team, _away_team, _competition)
VALUES
    -- Friday 20 March — played
    (2026, 'played',    '2026-03-20', '20:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'afc-bournemouth'),
     (SELECT id FROM team WHERE slug = 'manchester-united'),
     'premier-league'),

    -- Saturday 21 March — scheduled
    (2026, 'scheduled', '2026-03-21', '12:30:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'brighton-hove-albion'),
     (SELECT id FROM team WHERE slug = 'liverpool-fc'),
     'premier-league'),

    (2026, 'scheduled', '2026-03-21', '15:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'fulham-fc'),
     (SELECT id FROM team WHERE slug = 'burnley-fc'),
     'premier-league'),

    (2026, 'scheduled', '2026-03-21', '17:30:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'everton-fc'),
     (SELECT id FROM team WHERE slug = 'chelsea-fc'),
     'premier-league'),

    (2026, 'scheduled', '2026-03-21', '20:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'leeds-united'),
     (SELECT id FROM team WHERE slug = 'brentford-fc'),
     'premier-league'),

    -- Task description examples
    (2019, 'played', '2019-07-18', '18:30:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'fc-red-bull-salzburg'),
     (SELECT id FROM team WHERE slug = 'sk-sturm-graz'),
     'austrian-bundesliga'),

    (2019, 'played', '2019-10-23', '09:45:00',
     (SELECT id FROM sport WHERE name = 'Ice Hockey'),
     (SELECT id FROM team WHERE slug = 'kac-klagenfurt'),
     (SELECT id FROM team WHERE slug = 'vienna-capitals'),
     'ebel');

-- ── Events WITH stage columns ─────────────────────────────────────────────
-- AFC Champions League — from sample JSON
-- All stage values must reference existing rows in the stage table
INSERT INTO event (season, _status, event_date, event_time, _sport,
                   _home_team, _away_team, _competition,
                   _stage_id, _stage_competition)
VALUES
    (2024, 'played',    '2024-01-03', '00:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'al-shabab-fc'),
     (SELECT id FROM team WHERE slug = 'fc-nasaf-qarshi'),
     'afc-champions-league', 'ROUND OF 16', 'afc-champions-league'),

    (2024, 'scheduled', '2024-01-03', '16:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'al-hilal-saudi-fc'),
     (SELECT id FROM team WHERE slug = 'shabab-al-ahli-club'),
     'afc-champions-league', 'ROUND OF 16', 'afc-champions-league'),

    (2024, 'scheduled', '2024-01-04', '15:25:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'al-duhail-sc'),
     (SELECT id FROM team WHERE slug = 'al-rayyan-sc'),
     'afc-champions-league', 'ROUND OF 16', 'afc-champions-league'),

    (2024, 'scheduled', '2024-01-04', '08:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     (SELECT id FROM team WHERE slug = 'al-faisaly-fc'),
     (SELECT id FROM team WHERE slug = 'foolad-khuzestan-fc'),
     'afc-champions-league', 'ROUND OF 16', 'afc-champions-league'),

    -- FINAL: home team TBD — nullable _home_team handled by LEFT JOIN in queries
    (2024, 'scheduled', '2024-01-19', '00:00:00',
     (SELECT id FROM sport WHERE name = 'Football'),
     NULL,
     (SELECT id FROM team WHERE slug = 'urawa-red-diamonds'),
     'afc-champions-league', 'FINAL', 'afc-champions-league');

-- ── Results ───────────────────────────────────────────────────────────────
-- Bournemouth 2-2 Manchester United (Friday 20 March 2026)
-- NULL winner = draw
INSERT INTO event_result (_event, home_goals, away_goals, _winner_team)
VALUES (
           (SELECT id FROM event
            WHERE _home_team = (SELECT id FROM team WHERE slug = 'afc-bournemouth')
              AND   event_date  = '2026-03-20'),
           2, 2, NULL
       );

-- Al Shabab 1-2 Nasaf (AFC Champions League Round of 16)
INSERT INTO event_result (_event, home_goals, away_goals, _winner_team)
VALUES (
           (SELECT id FROM event
            WHERE _home_team = (SELECT id FROM team WHERE slug = 'al-shabab-fc')
              AND   event_date  = '2024-01-03'),
           1, 2,
           (SELECT id FROM team WHERE slug = 'fc-nasaf-qarshi')
       );

-- Salzburg vs Sturm (Austrian Bundesliga, 2019)
-- Result not in source data — seeded as 2-1 home win for demonstration
INSERT INTO event_result (_event, home_goals, away_goals, _winner_team)
VALUES (
           (SELECT id FROM event
            WHERE _home_team = (SELECT id FROM team WHERE slug = 'fc-red-bull-salzburg')
              AND   event_date  = '2019-07-18'),
           2, 1,
           (SELECT id FROM team WHERE slug = 'fc-red-bull-salzburg')
       );

-- KAC vs Vienna Capitals (ICE Hockey League, 2019)
-- Result not in source data — seeded as 3-2 home win for demonstration
INSERT INTO event_result (_event, home_goals, away_goals, _winner_team)
VALUES (
           (SELECT id FROM event
            WHERE _home_team = (SELECT id FROM team WHERE slug = 'kac-klagenfurt')
              AND   event_date  = '2019-10-23'),
           3, 2,
           (SELECT id FROM team WHERE slug = 'kac-klagenfurt')
       );