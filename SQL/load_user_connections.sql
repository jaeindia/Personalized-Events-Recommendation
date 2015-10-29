create database IF NOT exists EventsRecommendation;

use EventsRecommendation;


drop table user_connections;


create table user_connections(
    user_id BIGINT,
    friend BIGINT
);


load data local infile '/Users/Krish/Documents/Event-Recommendation/Dataset/user_connections.csv' into table user_connections
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(user_id,friend);


/* TEST */
SET SQL_SAFE_UPDATES = 0;

select * from user_connections limit 10;

select count(*) from user_connections;
