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

INSERT INTO sharing_zone(latitude, longitude, name)
SELECT latitude, longitude, name
FROM (
         SELECT 126.00001 AS latitude
              , 37.12     AS longitude
              , 'TEST 주차장1' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.00002 AS latitude
              , 37.12     AS longitude
              , 'TEST 주차장2' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.00003 AS latitude
         , 37.12 AS longitude
         , 'TEST 주차장3' AS name
         FROM DUAL
         UNION ALL
         SELECT 126.00004 AS latitude
                 , 37.12 AS longitude
                 , 'TEST 주차장4' AS name
         FROM DUAL
         UNION ALL
         SELECT 127.00001 AS latitude
                 , 37.12 AS longitude
                 , 'TEST 주차장5' AS name
         FROM DUAL
     ) A
WHERE NOT EXISTS (
    SELECT 1
    FROM sharing_zone
);
