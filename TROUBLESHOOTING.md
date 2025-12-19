# Troubleshooting 500 Error on Login

## Common Causes and Solutions

### 1. Email Service Configuration (Most Common)

**Problem**: Gmail SMTP requires an App Password, not your regular Gmail password.

**Solution**:
1. Go to your Google Account: https://myaccount.google.com/
2. Navigate to Security → 2-Step Verification (enable if not already)
3. Go to Security → App passwords
4. Generate a new app password for "Mail"
5. Copy the 16-character password
6. Update `application.properties`:
   ```properties
   spring.mail.password=your-16-char-app-password
   ```

### 2. Database Not Running

**Problem**: PostgreSQL database is not running.

**Solution**:
```bash
# Check if PostgreSQL is running
# Windows: Open Services and start PostgreSQL service
# Or use command:
pg_ctl -D "C:\Program Files\PostgreSQL\15\data" start

# Verify database exists
psql -U test -d pms
```

### 3. Database Connection Issues

**Problem**: Cannot connect to database.

**Solution**:
- Verify PostgreSQL is running on port 5432
- Check username/password in `application.properties`
- Ensure database `pms` exists:
  ```sql
  CREATE DATABASE pms;
  ```

### 4. Check Backend Logs

**To see the actual error**:
1. Look at your Spring Boot console output
2. The error will show the root cause
3. Common errors:
   - `AuthenticationFailedException` → Email password issue
   - `PSQLException` → Database connection issue
   - `NullPointerException` → Missing required field

## Quick Fix Applied

I've updated the code to:
1. **Catch email sending failures** - Login will proceed without 2FA if email service fails
2. **Better error messages** - The actual error message is now returned in the response
3. **Handle authentication errors** - BadCredentialsException returns 401 instead of 500

## Testing Steps

1. **Restart your Spring Boot application**
2. **Try logging in again**
3. **Check the browser console** - You should now see a more specific error message
4. **Check Spring Boot logs** - Look for the actual exception

## Temporary Workaround

If you want to disable 2FA temporarily to test login:

In `application.properties`, you can set user's `twoFactorEnabled` to `false` in the database:
```sql
UPDATE users SET two_factor_enabled = false WHERE email = 'your@email.com';
```

Or create a test user without 2FA in your database initialization script.
