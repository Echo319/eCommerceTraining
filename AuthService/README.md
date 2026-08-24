# Auth Service 

This service will be Oauth2 registering and checking JWT tokens of requests coming into the API gateway. 

As the rest of the services are on a internal network the token will likely be stripped from those requests? I guess 


# Plan

## 1 Service Setup & Boilerplate

- Create the auth-service Maven module/project.
  
- Add dependencies: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-security, spring-boot-starter-validation, postgresql, and jjwt (0.12.x).

## 2 Database & Schema (users table)

- Create the database migration script (V1__init_auth_schema.sql) for a users table (id, username, email, password, role, created_at).

- Create the corresponding User entity and UserRepository.

## 3 Core JWT Provider (JwtProvider)

- Implement token generation using your shared secret key.

- Embed custom claims (userId, roles, email) into the JWT payload alongside standard claims (sub, iat, exp).


## 4 Security & Hashing Setup

- Configure BCryptPasswordEncoder bean for hashing passwords before saving to the DB.

- Configure SecurityFilterChain to permit public access to /api/v1/auth/** endpoints.

## 5 Authentication Logic & Endpoints
- Build AuthService and AuthController exposing:POST /api/v1/auth/register $\rightarrow$ Validates input, checks for duplicates, hashes password, saves user, returns JWT.
- POST /api/v1/auth/login $\rightarrow$ Validates username/password against DB, returns JWT.

## 6 Gateway Integration

- Add the route predicate /api/v1/auth/** to ApiGateway/application.yml targeting http://auth-service:8082.

- Enable JwtAuthenticationFilter on protected endpoints in the Gateway to validate the token issued by your new Auth Service.