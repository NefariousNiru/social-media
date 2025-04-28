# Signup & OTP Verification Flow

## 1. Signup API

**Endpoint**: `POST /auth/signup`  
**Request**: JSON `{ email, username, password, firstName, lastName, dateOfBirth }`  
**Response**: `202 Accepted`

### Flow:
- Validate email and username are unique.
- Create a new user (password hashed, email marked as *unverified*).
- Generate a 10-character OTP.
- Store the OTP in Redis (with expiry).
- Send the OTP to the user's email via SMTP.

### On Email Send Failure:
- Signup still succeeds, but an error is logged.

---

## 2. OTP Verification API

**Endpoint**: `POST /auth/verify`  
**Request**: JSON `{ email, code }`  
**Response**: `200 OK` + `{ accessToken, refreshToken }`

### Flow:
- Validate the OTP (must match and not be expired).
- Mark the user's email as *verified*.
- Generate an Access Token (short-lived) and a Refresh Token (longer-lived).
- Store both tokens in Redis with TTL matching token expiry.

---

## 3. Token Generation & Session Storage

- **JWT** tokens are signed using an HMAC secret.
- Tokens contain `userId`, `issuedAt`, `expiresAt`, and `type` (access or refresh).
- Tokens are validated both via:
    - Signature and expiration check (JWT itself).
    - Redis session existence (extra security).

---

## 4. Key Error Handling

| Case                     | Behavior                        |
|---------------------------|---------------------------------|
| Email/Username already used | `400 Bad Request` with message |
| Invalid/Expired OTP       | `400 Bad Request` with message |
| Email send failure        | Log error, continue signup     |

---