# --- !Ups

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('50e2bb5a-1ab4-4b92-bab7-d57e2668fbe8', now(), 'Bite Bug', 1, 1, 3, 3, 5)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '50e2bb5a-1ab4-4b92-bab7-d57e2668fbe8',
    created_at   = now(),
    name         = 'Bite Bug',
    level        = 1,
    top_value    = 1,
    right_value  = 3,
    bottom_value = 3,
    left_value   = 5;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('03260fd7-8a04-4634-9d71-387923d4cf58', now(), 'Funguar', 1, 5, 1, 1, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '03260fd7-8a04-4634-9d71-387923d4cf58',
    created_at   = now(),
    name         = 'Funguar',
    level        = 1,
    top_value    = 5,
    right_value  = 1,
    bottom_value = 1,
    left_value   = 3;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('c2463a0e-b33a-48a8-a1cb-52a9d0a14d9d', now(), 'Geezard', 1, 1, 4, 1, 5)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'c2463a0e-b33a-48a8-a1cb-52a9d0a14d9d',
    created_at   = now(),
    name         = 'Geezard',
    level        = 1,
    top_value    = 1,
    right_value  = 4,
    bottom_value = 1,
    left_value   = 5;