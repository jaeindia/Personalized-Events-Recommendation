for f in users events user_friends event_attendees test train
do
    python scripts/store_db.py "./1000_rows/$f.csv" $f
done
