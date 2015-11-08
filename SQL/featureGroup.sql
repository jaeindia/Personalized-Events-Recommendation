create database IF NOT exists EventsRecommendation;

use EventsRecommendation;


drop table if exists featureGroup1;

create table featureGroup1(
event_id BIGINT NOT NULL,
invited Int,
yes int,
no int,
maybe int,
nochoice int
);

load data local infile '/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature1.csv' into table featureGroup1
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(event_id,invited,yes,no,maybe,nochoice);

Alter table featureGroup1 add index(event_id);


drop table if exists featureGroup2;

create table featureGroup2(
event_id BIGINT NOT NULL,
user_id BIGINT NOT NULL,
invited Int,
yes int,
no int,
maybe int,
nochoice int
);

load data local infile '/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature2.csv' into table featureGroup2
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(event_id,user_id,invited,yes,no,maybe,nochoice);

Alter table featureGroup2 add index(event_id);
Alter table featureGroup2 add index(user_id);


drop table if exists featureGroup4;

create table featureGroup4(
event_id BIGINT NOT NULL,
user_id BIGINT NOT NULL,
event_date BIGINT,
event_attendees int,
past_events int,
past_event_attendees int,
past_and_current_event_attendees int
);

load data local infile '/Users/Krish/Documents/Event-Recommendation/Dataset/Features/feature4.csv' into table featureGroup4
fields terminated by ','
enclosed by '"'
lines terminated by '\n'
(event_id,user_id,event_date,event_attendees,past_events,past_event_attendees,past_and_current_event_attendees);

Alter table featureGroup4 add index(event_id);
Alter table featureGroup4 add index(user_id);