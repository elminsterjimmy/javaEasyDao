drop table if exists `testMappingTable1`;
create table testMappingTable1(
id int PRIMARY KEY AUTO_INCREMENT,
t4_ref_id int NOT NULL,
name VARCHAR(30) NOT NULL
);

drop table if exists `testMappingTable2`;
create table testMappingTable2(
id int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL
);

drop table if exists `testMappingIntermediate12`;
create table testMappingIntermediate12(
id int PRIMARY KEY AUTO_INCREMENT,
m1Id int NOT NULL,
m2Id int NOT NULL
);

drop table if exists `testMappingTable3`;
create table testMappingTable3(
id int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL
);

drop table if exists `testMappingTable4`;
create table testMappingTable4(
id int PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(30) NOT NULL
);

drop table if exists `testMappingIntermediate13`;
create table testMappingIntermediate13(
id int PRIMARY KEY AUTO_INCREMENT,
m1Id int NOT NULL,
m2Id int NOT NULL
);