create database if not exists EventsRecommendation;

use EventsRecommendation;

Alter table event_attendees add index(event_id);
Alter table event_attendees add index(user_id);

Alter table events add index(id);
Alter table events add index(user_id);

Alter table user add index(user_id);




