create database testCleanse;

use testCleanse;

/*
user cols
user_id,locale,birthyear,gender,joinedAt,location,timezone
*/

/* DROP table */
drop table user;

/* CREATE table */
create table user(
    /*id INT NOT NULL AUTO_INCREMENT,*/
    user_id BIGINT NOT NULL,
    /*MAX length - 5 */
    locale VARCHAR(5),
    /* YEAR - YYYY*/
    birthyear INT,
    /* MAX length - 6 */
    gender VARCHAR(6),
    joinedAt VARCHAR(40),
    location VARCHAR(100),
    timezone INT
    /*PRIMARY KEY ( id )*/
);

/* LOAD csv into table */
load data local infile 'E:/Aug/KDD/data/users.csv' into table user
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(user_id,locale,birthyear,gender,joinedAt,location,timezone);
 
 
 
/* TEST */
select * from user limit 10;
 
select count(*) from user;
 
 
select max(length(locale)) from user;
select count(*) count, count(locale) locale from user;
 
select max(length(gender)) from user;
select count(*) count, count(gender) locale from user;

select max(length(convert(birthyear, char))) year from user;
select count(*) count, count(birthyear) year from user;

select distinct length(joinedAt) from user;
select count(*) count, count(joinedAt) joinedAt from user;

select max(length(location)) from user;
select count(*) count, count(location) location from user;
