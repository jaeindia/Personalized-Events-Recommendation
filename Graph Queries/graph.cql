CREATE INDEX ON :user_graph(user_id);
CREATE INDEX ON :event_graph(event_id);

//Users
LOAD CSV WITH HEADERS FROM "file:/Users/Krish/Documents/Event-Recommendation/Dataset/users.csv" as row CREATE (:user_graph {user_id: row.user_id, locale: row.locale, birth_year: row.birthyear, gender: row.gender, joined_at: row.joinedAt, location: row.location, timezone: row.timezone});

//Unique users
USING PERIODIC COMMIT LOAD CSV WITH HEADERS FROM "file:/Users/Krish/Documents/Event-Recommendation/Dataset/uniqueusers.csv" as row CREATE (:user_graph {user_id: row.user_id});

//Create friendship
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/Users/Krish/Documents/Event-Recommendation/Dataset/user_connections.csv" as row
MATCH (usera:user_graph{user_id: row.user})
MATCH (userb:user_graph{user_id: row.friend})
MERGE (usera)-[:FriendOf]->(userb)
MERGE (userb)-[:FriendOf]->(usera);

//Create event
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/Users/Krish/Documents/Event-Recommendation/Dataset/events_new.csv" as row
CREATE (:event_graph {event_id: row.events});


//Create event user relationship
USING PERIODIC COMMIT
LOAD CSV WITH HEADERS FROM "file:/Users/Krish/Documents/Event-Recommendation/Dataset/event_attendees_new.csv" as row
MATCH (event:event_graph{event_id: row.event_id})
MATCH (users:user_graph{user_id: row.user_id})
MERGE (event)-[:Invited{response:row.choice}]->(users);
