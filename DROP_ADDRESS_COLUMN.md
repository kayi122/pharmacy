# Fix Database - Drop Address Column

Run this SQL command in your PostgreSQL database (pgAdmin or any SQL client):

```sql
ALTER TABLE companies DROP COLUMN IF EXISTS address;
```

This will remove the NOT NULL constraint error you're experiencing.

After running this, restart your Spring Boot application.
