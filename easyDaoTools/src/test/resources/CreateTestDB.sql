drop table if exists `testtable`;
CREATE TABLE testtable
(
    id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    pass VARCHAR(30) NOT NULL,
    account DOUBLE DEFAULT 0,
    quary INT DEFAULT 0,
    tel VARCHAR(50),
    last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
