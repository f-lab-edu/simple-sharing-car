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