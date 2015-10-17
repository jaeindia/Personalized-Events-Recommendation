# Personalized-Events-Recommendation

## Store data to mongoDB:

Run:

```
python scripts/store_db.py <csv_file_name> <table_name>
```

For example:

```
python scripts/store_db.py ./1000_rows/users.csv users
```

Then, you can check the newly created collection in mongoDB:

```
mongo
> use cs5228
> db.users.count()
1000     # Meaning the script ran successfully
```

