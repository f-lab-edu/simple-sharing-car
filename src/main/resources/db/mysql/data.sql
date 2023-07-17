-- users
INSERT INTO users(id, email, password, name)
SELECT  1 AS id
        , 'admin@sharing.com' AS email
        , '$2a$10$snlebWOLmnAX52cNc6tos.2DWVL7kq2zmJVhRRmiQF/Wp1Qpq7MgS' AS password
        , 'Admin'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE ID = 1
);

-- sharing_zone
INSERT INTO sharing_zone(latitude, longitude, name)
SELECT latitude, longitude, name
FROM (
         SELECT 126.9287125421571 AS longitude
              , 37.35865146559683     AS latitude
              , 'TEST 주차장1' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.9287212421572 AS longitude
              , 37.35802136359653     AS latitude
              , 'TEST 주차장2' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.928730442157 AS longitude
         , 37.358422365596825 AS latitude
         , 'TEST 주차장3' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.92871444316704 AS longitude
                 , 37.35864136659682 AS longitude
                 , 'TEST 주차장4' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.928715442158 AS longitude
                 , 37.3586113656 AS latitude
                 , 'TEST 주차장5' AS name
         FROM DUAL
     ) A
WHERE NOT EXISTS (
    SELECT 1
    FROM sharing_zone
);

-- standard_car
INSERT INTO standard_car(type, model, price_per_minute)
SELECT type, model, price_per_minute
FROM (

         SELECT 'LIGHT_CAR' AS type
              , '모닝' AS model
              , 30 AS price_per_minute
         FROM DUAL
         UNION ALL
         SELECT 'LARGE_CAR' AS type
              , '그랜저' AS model
              , 70 AS price_per_minute
         FROM DUAL
         UNION ALL
         SELECT 'MIDSIZE_CAR' AS type
              , '산타페' AS model
              , 50 AS price_per_minute
         FROM DUAL
         UNION ALL
         SELECT 'SEMI_MIDSIZE_CAR' AS type
              , '아반떼' AS model
              , 40 AS price_per_minute
         FROM DUAL
     ) A
WHERE NOT EXISTS (
        SELECT 1
        FROM standard_car
    );

-- sharing_car
INSERT INTO sharing_car(standard_car_id, sharing_zone_id)
SELECT standard_car_id, sharing_zone_id
FROM (

         SELECT 1 AS standard_car_id
              , 1 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 1 AS standard_car_id
              , 2 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 2 AS standard_car_id
              , 1 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 2 AS standard_car_id
              , 2 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 1 AS standard_car_id
              , 3 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 2 AS standard_car_id
              , 3 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 3 AS standard_car_id
              , 3 AS sharing_zone_id
         FROM DUAL
         UNION ALL
         SELECT 4 AS standard_car_id
              , 3 AS sharing_zone_id
         FROM DUAL
     ) A
WHERE NOT EXISTS (
        SELECT 1
        FROM sharing_car
    );

-- 임시 reservation
DELETE FROM reservation WHERE id = 1;
DELETE FROM reservation WHERE id = 2;
DELETE FROM reservation WHERE id = 3;

INSERT INTO reservation(id, sharing_car_id, res_start_time, res_end_time, status)
VALUES(1, 1, date_add(now(), interval -1 hour), date_add(date_add(now(), interval 1 hour), interval -1 second), 'RESERVED');

INSERT INTO reservation(id, sharing_car_id, res_start_time, res_end_time, status)
VALUES(2, 1, date_add(now(), interval 1 hour), date_add(date_add(now(), interval 2 hour), interval -1 second), 'RESERVED');

INSERT INTO reservation(id, sharing_car_id, res_start_time, res_end_time, status)
VALUES(3, 2, date_add(now(), interval -1 hour), date_add(date_add(now(), interval 1 hour), interval -1 second), 'RESERVED');
