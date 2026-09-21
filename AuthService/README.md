# Auth Service

The Auth Service is a training-focused Spring Boot application responsible for registering users, authenticating them, and issuing JWTs for use by the API Gateway and protected downstream services.

This service is not intended to be a full production identity platform. Instead, it demonstrates the core patterns of:
- user registration
- password hashing
- login validation
- JWT generation
- role-based authorization
- integration with an API Gateway that enforces access policies

---


The API Gateway is responsible for validating incoming JWTs and applying authorization rules to protected routes.

---

## Architecture

The authentication flow is:

1. Client calls `POST /api/v1/auth/register` or `POST /api/v1/auth/login`
2. Auth Service validates the request and user credentials
3. Auth Service hashes the password and saves the user if needed
4. Auth Service creates a JWT containing the username, email, user ID, and role
5. API Gateway validates the JWT on protected endpoints
6. Gateway enforces access rules such as:
    - public: `/api/v1/auth/**`
    - authenticated: `GET /api/v1/products/**`
    - admin only: product/category writes

---

## Features

- User registration
- Login with username and password
- Password hashing with BCrypt
- JWT creation and validation
- Default user role assignment
- Role-based identity model (`ROLE_USER`, `ROLE_ADMIN`)
- Database-backed persistence using PostgreSQL
- Flyway migration support
- Spring Security configuration for stateless authentication

---

## API Endpoints

### Public endpoints

#### Register a user
`POST /api/v1/auth/register`

Request body:
```json
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "StrongPassword123"
}
```

Successful response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tokenType": "Bearer",
  "expiresInMs": 9000000,
  "username": "alice",
  "role": "ROLE_USER"
}
```

If the username or email is already in use, the API returns a conflict response.

---

#### Login
`POST /api/v1/auth/login`

Request body:
```json
{
  "username": "alice",
  "password": "StrongPassword123"
}
```

Successful response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9....",
  "tokenType": "Bearer",
  "expiresInMs": 9000000,
  "username": "alice",
  "role": "ROLE_USER"
}
```

Invalid credentials return an unauthorized response.

---

## Security model

This service uses a stateless JWT model:

- CSRF is disabled because the API is token-based
- HTTP sessions are disabled (`STATELESS`)
- public endpoints are restricted to the auth flows
- all other application traffic should be validated by the gateway before it reaches protected services

The Auth Service itself is deliberately designed to handle identity concerns only. It does not act as the sole enforcement layer for every protected route in the ecosystem. Instead, the API Gateway is the main enforcement point for business/route authorization.

---

## JWT details

The service generates JWT tokens using a shared secret key configured from the environment.

The token includes:
- subject: username
- claims:
    - `userId`
    - `email`
    - `role`

A token is also configured with:
- issued time
- expiration time

---

## Database

The service uses PostgreSQL and Flyway for schema management.

### Core tables
- `roles`
- `users`

The `users` table stores:
- username
- email
- password hash
- role reference
- active flag
- created/updated timestamps

Initial role records are inserted by migration:
- `ROLE_USER`
- `ROLE_ADMIN`

---

## Configuration

The service depends on the following environment variables:

```bash
DB_HOST=localhost
DB_PORT=5432
DB_USER=root
DB_PASSWORD=changeme

JWT_SECRET=your-shared-secret-key
JWT_EXPIRATION_MS=9000000
```

Example application config:

```yaml
jwt:
  secret: ${JWT_SECRET}
  expiration-ms: ${JWT_EXPIRATION_MS:9000000}
```

The shared JWT secret must be the same value used by the API Gateway when validating tokens.

---

## Local development

### Run the service
```bash
mvn spring-boot:run
```

### Default port
```text
http://localhost:8080
```

## Related components

- `APIGateway` validates JWTs and enforces route-level authorization
- `ProductCatalog` and other services use the gateway as the protected entry point
- `AuthService` is responsible for identity, credentials, and token issuance

---
