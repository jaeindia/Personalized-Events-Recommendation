USE eventsrecommendation;

/*
Number of users attending, not attending, maybe attending and invited to the event;
*/
SELECT event_id,
	count(*) 'INVITED',
	sum(CASE 
			WHEN user_choice = 'YES'
				THEN 1
			ELSE 0
			END) 'YES',
	sum(CASE 
			WHEN user_choice = 'NO'
				THEN 1
			ELSE 0
			END) 'NO',
	sum(CASE 
			WHEN user_choice = 'MAYBE'
				THEN 1
			ELSE 0
			END) 'MAY BE',
	sum(CASE 
			WHEN user_choice = ''
				THEN 1
			ELSE 0
			END) 'NO RESPONSE'
FROM event_attendees
GROUP BY event_id;

/*
Number of friends attending, not attending, maybe attending and invited to the event
*/

SELECT count(event_attendees.user_id) 'INVITED',
	sum(CASE 
			WHEN event_attendees.user_choice = 'YES'
				THEN 1
			ELSE 0
			END) 'YES',
	sum(CASE 
			WHEN event_attendees.user_choice = 'NO'
				THEN 1
			ELSE 0
			END) 'NO',
	sum(CASE 
			WHEN event_attendees.user_choice = 'MAYBE'
				THEN 1
			ELSE 0
			END) 'MAY BE',
	sum(CASE 
			WHEN event_attendees.user_choice = ''
				THEN 1
			ELSE 0
			END) 'NO RESPONSE'
FROM user,
	user_connections
LEFT JOIN event_attendees
	ON user_connections.friend = event_attendees.user_id
WHERE user.user_id = user_connections.user_id
	AND user_connections.friend = event_attendees.user_id
	AND user.user_id = 3197468391
	AND event_attendees.event_id = 2778259784;