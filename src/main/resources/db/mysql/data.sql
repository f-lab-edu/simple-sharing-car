INSERT INTO users(id, email, password, name)
SELECT  1 AS id
        , 'admin@sharing.com' AS email
        , '1234' AS password
        , 'Admin'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1
    FROM users
    WHERE ID = 1
);