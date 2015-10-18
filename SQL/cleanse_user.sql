DROP DATABASE IF EXISTS EventsRecommendation;

CREATE DATABASE EventsRecommendation;

USE EventsRecommendation;

/*
user cols
user_id,locale,birthyear,gender,joinedAt,location,timezone
*/
/* DROP table */
DROP TABLE IF EXISTS user;

/* CREATE table */
CREATE TABLE user (
	/*id INT NOT NULL AUTO_INCREMENT,*/
	user_id BIGINT NOT NULL,
	/*MAX length - 5 */
	locale VARCHAR(5),
	/* YEAR - YYYY*/
	birthyear INT,
	/* MAX length - 6 */
	gender VARCHAR(6), joinedAt VARCHAR(40), location VARCHAR(100), timezone INT
	/*PRIMARY KEY ( id )*/
	);

/* LOAD csv into table */
LOAD data LOCAL infile 'E:/Aug/KDD/data/users.csv'
INTO TABLE user 
fields terminated BY ',' 
enclosed BY '"' 
lines terminated BY '\n' 
(user_id, locale, birthyear, gender, joinedAt, location, timezone);

/* TEST */
SELECT *
FROM user limit 10;

SELECT count(*)
FROM user;

SELECT max(length(locale))
FROM user;

SELECT count(*) count, count(locale) locale
FROM user;

SELECT max(length(gender))
FROM user;

SELECT count(*) count, count(gender) locale
FROM user;

SELECT max(length(convert(birthyear, CHAR))) year
FROM user;

SELECT count(*) count, count(birthyear) year
FROM user;

SELECT DISTINCT length(joinedAt)
FROM user;

SELECT count(*) count, count(joinedAt) joinedAt
FROM user;

SELECT max(length(location))
FROM user;

SELECT count(*) count, count(location) location
FROM user;
