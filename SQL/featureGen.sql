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
