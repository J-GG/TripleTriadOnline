/****************
* Cards
****************/

# --- !Ups

/* TESTS */

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('06cb8927-921e-4147-a60e-a5929441ae44', now(), 'Blogging Shiba', 1, 4, 2, 4, 10)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '06cb8927-921e-4147-a60e-a5929441ae44',
    created_at   = now(),
    name         = 'Blogging Shiba',
    level        = 1,
    top_value    = 4,
    right_value  = 2,
    bottom_value = 4,
    left_value   = 10;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('03f1571f-5a28-472c-953f-c4403ca3911e', now(), 'Doge Meme', 1, 2, 2, 2, 2)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '03f1571f-5a28-472c-953f-c4403ca3911e',
    created_at   = now(),
    name         = 'Doge Meme',
    level        = 1,
    top_value    = 2,
    right_value  = 2,
    bottom_value = 2,
    left_value   = 2;


INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('2c55d097-7350-4d19-a6ee-31b29d627b97', now(), 'Pandaminator', 1, 3, 3, 5, 2)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '2c55d097-7350-4d19-a6ee-31b29d627b97',
    created_at   = now(),
    name         = 'Pandaminator',
    level        = 1,
    top_value    = 3,
    right_value  = 3,
    bottom_value = 5,
    left_value   = 2;


/* LEVEL 1 */
/*
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
VALUES ('09784985-00e4-4959-b239-f8afa0ea8466', now(), 'Red Bat', 1, 6, 1, 1, 2)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '09784985-00e4-4959-b239-f8afa0ea8466',
    created_at   = now(),
    name         = 'Red Bat',
    level        = 1,
    top_value    = 6,
    right_value  = 1,
    bottom_value = 1,
    left_value   = 2;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('1d8511b6-4d18-46eb-b05b-9e35fb96027c', now(), 'Blobra', 1, 2, 3, 1, 5)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '1d8511b6-4d18-46eb-b05b-9e35fb96027c',
    created_at   = now(),
    name         = 'Blobra',
    level        = 1,
    top_value    = 2,
    right_value  = 3,
    bottom_value = 1,
    left_value   = 5;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('b81a045d-5fea-4d31-9abd-f51f284d0b4c', now(), 'Gayla', 1, 2, 1, 4, 4)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'b81a045d-5fea-4d31-9abd-f51f284d0b4c',
    created_at   = now(),
    NAME         = 'Gayla',
    LEVEL        = 1,
    top_value    = 2,
    right_value  = 1,
    bottom_value = 4,
    left_value   = 4;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('b57b0cf2-8a6f-49e5-9528-9df81f1277a1', now(), 'Gesper', 1, 1, 5, 4, 1)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'b57b0cf2-8a6f-49e5-9528-9df81f1277a1',
    created_at   = now(),
    NAME         = 'Gesper',
    LEVEL        = 1,
    top_value    = 1,
    right_value  = 5,
    bottom_value = 4,
    left_value   = 1;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('15faf8c3-cacc-4c4d-977a-9217558d46a5', now(), 'Fastitocalon-F', 1, 3, 5, 2, 1)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '15faf8c3-cacc-4c4d-977a-9217558d46a5',
    created_at   = now(),
    NAME         = 'Fastitocalon-F',
    LEVEL        = 1,
    top_value    = 3,
    right_value  = 5,
    bottom_value = 2,
    left_value   = 1;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('abddddd6-a947-4a72-9772-75be0ac685d6', now(), 'Blood Soul', 1, 2, 1, 6, 1)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'abddddd6-a947-4a72-9772-75be0ac685d6',
    created_at   = now(),
    NAME         = 'Blood Soul',
    LEVEL        = 1,
    top_value    = 2,
    right_value  = 1,
    bottom_value = 6,
    left_value   = 1;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('108b98b2-86ea-4d26-9aec-9262aa04c086', now(), 'Caterchipillar', 1, 4, 2, 4, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '108b98b2-86ea-4d26-9aec-9262aa04c086',
    created_at   = now(),
    NAME         = 'Caterchipillar',
    LEVEL        = 1,
    top_value    = 4,
    right_value  = 2,
    bottom_value = 4,
    left_value   = 3;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('ca02105e-7551-4442-836e-5c99186b95de', now(), 'Cockatrice', 1, 2, 1, 2, 6)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'ca02105e-7551-4442-836e-5c99186b95de',
    created_at   = now(),
    NAME         = 'Cockatrice',
    LEVEL        = 1,
    top_value    = 2,
    right_value  = 1,
    bottom_value = 2,
    left_value   = 6;
*/

/* LEVEL 2 */
/*
INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('365d77e2-bbf9-44e2-99c3-1b791164d187', now(), 'Grat', 2, 7, 1, 3, 1)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '365d77e2-bbf9-44e2-99c3-1b791164d187',
    created_at   = now(),
    name         = 'Grat',
    level        = 2,
    top_value    = 7,
    right_value  = 1,
    bottom_value = 3,
    left_value   = 1;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('e55b1b00-64b2-4f40-b53e-a745ea3e5208', now(), 'Buel', 2, 6, 2, 2, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'e55b1b00-64b2-4f40-b53e-a745ea3e5208',
    created_at   = now(),
    name         = 'Buel',
    level        = 2,
    top_value    = 6,
    right_value  = 2,
    bottom_value = 2,
    left_value   = 3;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('c4eccdcc-f775-40ac-80a8-6116c1b83156', now(), 'Mesmerize', 2, 5, 3, 3, 4)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'c4eccdcc-f775-40ac-80a8-6116c1b83156',
    created_at   = now(),
    name         = 'Mesmerize',
    level        = 2,
    top_value    = 5,
    right_value  = 3,
    bottom_value = 3,
    left_value   = 4;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('6f90e524-a4b2-41d2-8958-f83fe57f226a', now(), 'Glacial Eye', 2, 6, 1, 4, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '6f90e524-a4b2-41d2-8958-f83fe57f226a',
    created_at   = now(),
    name         = 'Glacial Eye',
    level        = 2,
    top_value    = 6,
    right_value  = 1,
    bottom_value = 4,
    left_value   = 3;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('e7775b07-c6ab-43c9-8434-cecce317d99c', now(), 'Bellhelmel', 2, 3, 4, 5, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'e7775b07-c6ab-43c9-8434-cecce317d99c',
    created_at   = now(),
    name         = 'Bellhelmel',
    level        = 2,
    top_value    = 3,
    right_value  = 4,
    bottom_value = 5,
    left_value   = 3;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('535b879d-f38b-4211-b4cf-7b6983dc28ce', now(), 'Thrustaevis', 2, 5, 3, 2, 5)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '535b879d-f38b-4211-b4cf-7b6983dc28ce',
    created_at   = now(),
    NAME         = 'Thrustaevis',
    LEVEL        = 2,
    top_value    = 5,
    right_value  = 3,
    bottom_value = 2,
    left_value   = 5;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('ba6d672c-6d0e-4076-b96b-125fced10be0', now(), 'Anacondaur', 2, 5, 1, 3, 5)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'ba6d672c-6d0e-4076-b96b-125fced10be0',
    created_at   = now(),
    NAME         = 'Anacondaur',
    LEVEL        = 2,
    top_value    = 5,
    right_value  = 1,
    bottom_value = 3,
    left_value   = 5;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('7e4d77ed-f957-4bd2-9dbb-068d7eff68bc', now(), 'Creeps', 2, 5, 2, 5, 2)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '7e4d77ed-f957-4bd2-9dbb-068d7eff68bc',
    created_at   = now(),
    NAME         = 'Creeps',
    LEVEL        = 2,
    top_value    = 5,
    right_value  = 2,
    bottom_value = 5,
    left_value   = 2;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('f25fa57d-0e2c-42a3-88b3-cd880af8ff8b', now(), 'Grendel', 2, 4, 4, 5, 2)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'f25fa57d-0e2c-42a3-88b3-cd880af8ff8b',
    created_at   = now(),
    NAME         = 'Grendel',
    LEVEL        = 2,
    top_value    = 4,
    right_value  = 4,
    bottom_value = 5,
    left_value   = 2;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('86badc59-87ee-4747-a498-681033c7610c', now(), 'Jelleye', 2, 3, 2, 1, 7)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = '86badc59-87ee-4747-a498-681033c7610c',
    created_at   = now(),
    NAME         = 'Jelleye',
    LEVEL        = 2,
    top_value    = 3,
    right_value  = 2,
    bottom_value = 1,
    left_value   = 7;

INSERT INTO
  card (uid, created_at, name, level, top_value, right_value, bottom_value, left_value)
VALUES ('b222b061-6971-4703-a8c8-a7a9cdd0aa72', now(), 'Grand Mantis', 2, 5, 2, 5, 3)
ON CONFLICT (uid)
  DO UPDATE SET
    uid          = 'b222b061-6971-4703-a8c8-a7a9cdd0aa72',
    created_at   = now(),
    NAME         = 'Grand Mantis',
    LEVEL        = 2,
    top_value    = 5,
    right_value  = 2,
    bottom_value = 5,
    left_value   = 3;
*/

# --- !Downs

/* TESTS */
DELETE FROM card
WHERE uid = '03f1571f-5a28-472c-953f-c4403ca3911e';
DELETE FROM card
WHERE uid = '2c55d097-7350-4d19-a6ee-31b29d627b97';
DELETE FROM card
WHERE uid = '06cb8927-921e-4147-a60e-a5929441ae44';

/* LEVEL 1*/
/*DELETE FROM card
WHERE uid = 'c2463a0e-b33a-48a8-a1cb-52a9d0a14d9d';
DELETE FROM card
WHERE uid = '03260fd7-8a04-4634-9d71-387923d4cf58';
DELETE FROM card
WHERE uid = '50e2bb5a-1ab4-4b92-bab7-d57e2668fbe8';
DELETE FROM card
WHERE uid = '09784985-00e4-4959-b239-f8afa0ea8466';
DELETE FROM card
WHERE uid = 'b81a045d-5fea-4d31-9abd-f51f284d0b4c';
DELETE FROM card
WHERE uid = 'b57b0cf2-8a6f-49e5-9528-9df81f1277a1';
DELETE FROM card
WHERE uid = '15faf8c3-cacc-4c4d-977a-9217558d46a5';
DELETE FROM card
WHERE uid = 'abddddd6-a947-4a72-9772-75be0ac685d6';
DELETE FROM card
WHERE uid = '108b98b2-86ea-4d26-9aec-9262aa04c086';
DELETE FROM card
WHERE uid = 'ca02105e-7551-4442-836e-5c99186b95de';
DELETE FROM card
WHERE uid = '1d8511b6-4d18-46eb-b05b-9e35fb96027c';
*/

/* LEVEL 2 */
/*
DELETE FROM card
WHERE uid = '6f90e524-a4b2-41d2-8958-f83fe57f226a';
DELETE FROM card
WHERE uid = 'e7775b07-c6ab-43c9-8434-cecce317d99c';
DELETE FROM card
WHERE uid = '535b879d-f38b-4211-b4cf-7b6983dc28ce';
DELETE FROM card
WHERE uid = 'ba6d672c-6d0e-4076-b96b-125fced10be0';
DELETE FROM card
WHERE uid = '7e4d77ed-f957-4bd2-9dbb-068d7eff68bc';
DELETE FROM card
WHERE uid = 'f25fa57d-0e2c-42a3-88b3-cd880af8ff8b';
DELETE FROM card
WHERE uid = '86badc59-87ee-4747-a498-681033c7610c';
DELETE FROM card
WHERE uid = 'b222b061-6971-4703-a8c8-a7a9cdd0aa72';
DELETE FROM card
WHERE uid = '365d77e2-bbf9-44e2-99c3-1b791164d187';
DELETE FROM card
WHERE uid = 'e55b1b00-64b2-4f40-b53e-a745ea3e5208';
DELETE FROM card
WHERE uid = 'c4eccdcc-f775-40ac-80a8-6116c1b83156';
*/