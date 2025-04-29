### 🔐 **Authentication System Overview**

> 🔐 Implemented a complete stateless JWT-based authentication system using Spring Boot.  
> OTP-based email verification and password reset via Redis TTL.  
> Used AOP-driven dynamic rate limiting on sensitive routes.  
> All session tokens tracked and managed in Redis with secure rotation and invalidation logic.


| **Feature** | **What It Does** | **Tech / Library Used** | **How I Implemented It**                                            |
|-------------|------------------|--------------------------|---------------------------------------------------------------------|
| **User Signup** | Register a new user | `Spring Boot`, `JPA` | `UserService.createUser()` persists user with hashed password       |
| **Email Verification** | Send OTP to confirm email | `Redis`, `SMTP (JavaMail)` | `OtpService` generates and stores OTP; `SmtpEmailService` emails it |
| **OTP Expiry & Validation** | Auto-expire OTP after 10 mins | `Redis TTL`, `@RedisHash` | OTP stored with 600s TTL and validated via email lookup             |
| **Login** | User signs in with email & password | `BCrypt`, `Spring Security` | Password is hashed/verified; also checks `isEmailVerified` flag     |
| **Access & Refresh Tokens** | Stateless session management | `JWT`, `Redis`, `HMAC256` | Issued signed tokens with type claims, stored in Redis              |
| **Session Storage** | Link tokens to users in Redis | `Redis`, `UUID` | `SessionService` stores `access:` and `refresh:` token keys         |
| **Token Revocation** | Logout or refresh invalidates token | `Redis.delete(...)` | Session entries removed on logout or rotation                       |
| **Refresh Token Flow** | Reissue tokens securely | `JWT`, `Redis`, `AOP` | Validates old token, revokes it, issues new access+refresh pair     |
| **Forgot Password** | OTP flow to reset password | `Redis`, `Email`, `AOP` | Sends OTP, validates, and resets password securely                  |
| **Rate Limiting** | Prevent brute force and spam | `Spring AOP`, `Redis`, `@RateLimit` | Custom annotation reads limits from `application.properties`        |
| **Custom Error Handling** | Unified error codes/responses | `@ControllerAdvice`, `ErrorCode enum` | `GlobalExceptionHandler` sends structured `BusinessError` JSON      |
| **Security Filter** | Protect routes using Bearer Token | `OncePerRequestFilter`, `SecurityContext` | `AuthTokenBearer` reads and validates tokens from headers           |
| **Stateless Auth Config** | Allow only token-based access | `SecurityFilterChain` | CSRF disabled, sessions stateless, `/auth/**` endpoints public      |
| **AOP-Based Rate Limiter** | Reusable and declarative limits | `@RateLimit`, `SpEL`, `Redis` | Limits like login attempts, resend OTP, refresh, etc.               |
| **Token Cleanup on Reset** | Logout from all devices after password change | `Redis list`, `SessionService.invalidateAllSessionsForUser()` | Tokens grouped under `user-sessions:{userId}` and bulk deleted      |
---