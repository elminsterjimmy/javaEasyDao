drop table if exists `testtable`;
create table testtable(
id int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL,
pass VARCHAR(30) NOT NULL,
account double default 0,
quary int default 0,
tel VARCHAR(50),
last_update TIMESTAMP
);