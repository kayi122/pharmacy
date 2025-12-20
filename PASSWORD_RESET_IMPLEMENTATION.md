# Password Reset Implementation with Email Links

## Overview
This implementation provides a secure password reset functionality for both Users and Customers using email links with tokens.

## Features
- ✅ Token-based password reset (1-hour expiration)
- ✅ Email link sent to user's email
- ✅ Works for both Users and Customers
- ✅ Token validation before password reset
- ✅ One-time use tokens
- ✅ Secure token generation using UUID

## Backend Implementation

### 1. Database Setup
Run the SQL migration script to create the password_reset_tokens table:

```bash
mysql -u your_username -p your_database < create_password_reset_tokens_table.sql
```

Or execute manually:
```sql
CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    INDEX idx_token (token),
    INDEX idx_email (email)
);
```

### 2. New Files Created
- `PasswordResetToken.java` - Entity model
- `PasswordResetTokenRepository.java` - Repository interface
- `ResetPasswordPage.js` - Frontend component

### 3. Modified Files
- `EmailService.java` - Added `sendPasswordResetLink()` method
- `AuthService.java` - Updated password reset logic
- `AuthController.java` - Added `/validate-reset-token` endpoint
- `PasswordResetRequest.java` - Changed from email+OTP to token-based
- `ForgotPassword.js` - Simplified to only request email
- `App.js` - Added reset password route

### 4. API Endpoints

#### Request Password Reset
```
POST /api/auth/forgot-password?email={email}
Response: "Password reset link sent to your email"
```

#### Validate Reset Token
```
GET /api/auth/validate-reset-token?token={token}
Response: "Token is valid" or error message
```

#### Reset Password
```
POST /api/auth/reset-password
Body: {
  "token": "uuid-token-here",
  "newPassword": "newPassword123"
}
Response: {
  "message": "Password reset successful! Please login with your new password."
}
```

## Frontend Implementation

### 1. Forgot Password Flow
1. User clicks "Forgot Password" on login page
2. User enters their email address
3. System sends reset link to email
4. User receives email with link: `http://localhost:3000/reset-password?token=xxx`

### 2. Reset Password Flow
1. User clicks link in email
2. System validates token
3. If valid, user enters new password
4. Password is updated
5. User is redirected to login

### 3. Components

#### ForgotPassword Component
- Simple email input form
- Sends reset link request
- Shows success message

#### ResetPasswordPage Component
- Validates token on load
- Shows password input form if valid
- Handles password reset
- Redirects to login on success

## Email Configuration

Make sure your `application.properties` has email configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## Security Features

1. **Token Expiration**: Tokens expire after 1 hour
2. **One-Time Use**: Tokens can only be used once
3. **Secure Generation**: Uses UUID for random token generation
4. **Email Verification**: Only registered emails can request reset
5. **User Type Tracking**: Tracks whether token is for User or Customer

## Testing

### 1. Test Forgot Password
```bash
curl -X POST "http://localhost:8080/api/auth/forgot-password?email=user@example.com"
```

### 2. Test Token Validation
```bash
curl -X GET "http://localhost:8080/api/auth/validate-reset-token?token=your-token-here"
```

### 3. Test Password Reset
```bash
curl -X POST "http://localhost:8080/api/auth/reset-password" \
  -H "Content-Type: application/json" \
  -d '{
    "token": "your-token-here",
    "newPassword": "newPassword123"
  }'
```

## Frontend URLs

- Forgot Password: `http://localhost:3000` (click "Forgot Password")
- Reset Password: `http://localhost:3000/reset-password?token=xxx` (from email link)

## Troubleshooting

### Email Not Sending
- Check email configuration in `application.properties`
- Verify SMTP credentials
- Check spam folder
- Enable "Less secure app access" for Gmail (or use App Password)

### Token Invalid
- Check if token has expired (1 hour limit)
- Verify token hasn't been used already
- Ensure token exists in database

### Database Issues
- Run the migration script
- Check database connection
- Verify table was created successfully

## Production Considerations

1. **Change Frontend URL**: Update reset link in `AuthService.java`:
   ```java
   String resetLink = "https://yourdomain.com/reset-password?token=" + token;
   ```

2. **Adjust Token Expiration**: Modify in `PasswordResetToken.java`:
   ```java
   expiryDate = LocalDateTime.now().plusHours(24); // 24 hours instead of 1
   ```

3. **Add Rate Limiting**: Prevent abuse by limiting reset requests per email

4. **Use HTML Emails**: Enhance email template with HTML formatting

5. **Add Logging**: Log all password reset attempts for security auditing

## Support

For issues or questions, contact the development team.
