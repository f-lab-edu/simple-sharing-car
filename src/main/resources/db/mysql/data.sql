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
