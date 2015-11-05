//Feature one

Match p=(event:event_graph)-[rel:Invited]->(users:user_graph)
return event.event_id as Event,count(rel) as Invited, sum(case rel.response when 'YES' then 1 else 0 end) as YES,sum(case rel.response when 'NO' then 1 else 0 end) as NO,sum(case rel.response when 'MAYBE' then 1 else 0 end) as MAYBE,sum(case rel.response when 'NOCHOICE' then 1 else 0 end) as NOCHOICE order by event.event_id

//Feature one for new Schema

Match p=(event:Event)-[rel:Invited]->(users:User)
return event.eventId as Event,count(rel) as Invited, sum(case rel.response when 'YES' then 1 else 0 end) as YES,sum(case rel.response when 'NO' then 1 else 0 end) as NO,sum(case rel.response when 'MAYBE' then 1 else 0 end) as MAYBE,sum(case rel.response when 'NOCHOICE' then 1 else 0 end) as NOCHOICE order by event.eventId

//Feature two - entire dataset - old schema

Match p=(event:event_graph)-[rel:Invited]->(users:user_graph)-->(fusers:user_graph)<-[frel:Invited]-(fevent:event_graph)
return event.event_id as Event,users.user_id as User,collect(distinct(fusers.user_id)) as Friends, sum(case frel.response when 'YES' then (case when (event.event_id = fevent.event_id) then 1 else 0 end) else 0 end) as YES,sum(case frel.response when 'NO' then (case when (event.event_id = fevent.event_id) then 1 else 0 end) else 0 end) as NO,sum(case frel.response when 'MAYBE' then (case when (event.event_id = fevent.event_id) then 1 else 0 end) else 0 end) as MAYBE,sum(case frel.response when 'NOCHOICE' then (case when (event.event_id = fevent.event_id) then 1 else 0 end) else 0 end) as NOCHOICE  order by event.event_id,users.user_id


// For one event

Match p=(event:Event)-[rel:Invited]->(users:User)-->(fusers:User)<-[frel:Invited]-(fevent:Event)
where id(event) = 10000 and id(event) = id(fevent)
return event.eventId as Event,users.userId as User,count(distinct(fusers.userId)) as Friends, sum(case frel.response when 'YES' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as YES,sum(case frel.response when 'NO' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as NO,sum(case frel.response when 'MAYBE' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as MAYBE,sum(case frel.response when 'NOCHOICE' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as NOCHOICE  order by event.eventId,users.userId

// For range of events

Match p=(event:Event)-[rel:Invited]->(users:User)-->(fusers:User)<-[frel:Invited]-(fevent:Event)
where id(event) > 400000 and id(event) < 500000 and id(event) = id(fevent)
return event.eventId as Event,users.userId as User,count(distinct(fusers.userId)) as Friends, sum(case frel.response when 'YES' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as YES,sum(case frel.response when 'NO' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as NO,sum(case frel.response when 'MAYBE' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as MAYBE,sum(case frel.response when 'NOCHOICE' then (case when (event.eventId = fevent.eventId) then 1 else 0 end) else 0 end) as NOCHOICE  order by event.eventId,users.userId
