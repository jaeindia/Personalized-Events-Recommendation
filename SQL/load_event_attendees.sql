create database IF NOT exists EventsRecommendation;

use EventsRecommendation;


drop table event_attendees;


create table event_attendees(
    event_id BIGINT,
    user_id BIGINT,
    user_choice VARCHAR(7)
);


load data local infile '/Users/Krish/Documents/Event-Recommendation/Dataset/event_attendees.csv' into table event_attendees
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(event_id,user_id,user_choice);


/* TEST */
SET SQL_SAFE_UPDATES = 0;

select * from event_attendees limit 10;

select count(*) from event_attendees;
