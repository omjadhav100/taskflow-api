# TaskFlow — Full-Stack Task Management App

A Trello-style task board built to practice production-style full-stack
development: Spring Boot REST API, JWT authentication, PostgreSQL, and a
React frontend.

**Live demo:** "coming soon"
*(Backend is on Render's free tier — first request may take 30-60s to wake up.)*

## What it does

- Users register and log in (JWT-based authentication, BCrypt password hashing)
- Each user has their own Boards
- Boards contain Lists (e.g. "To Do", "In Progress", "Done")
- Lists contain Tasks (title, status, due date)
- Full CRUD on all entities, with ownership enforced at the API level —
  users can only see and modify their own data

## Tech stack

**Backend:** Java 17, Spring Boot 3, Spring Data JPA, Spring Security, JWT (jjwt), PostgreSQL, Maven
**Frontend:** React, Vite, Axios
**Testing:** JUnit 5, Mockito
**Deployment:** Render (backend + frontend), Neon (PostgreSQL)

## Architecture

```
React (Axios) → REST API (Spring Boot) → PostgreSQL
```

The backend follows a layered architecture:

```
Controller  → handles HTTP only (routes, status codes, request/response shape)
Service     → business logic, validation, ownership checks
Repository  → data access via Spring Data JPA
Entity      → JPA-mapped domain objects
DTO         → request/response shapes, decoupled from entities
```

A `JwtAuthFilter` runs on every request, validating the token and attaching
the authenticated user to Spring Security's context — no session state is
kept server-side (`SessionCreationPolicy.STATELESS`).

## API overview

```
POST   /api/auth/register
POST   /api/auth/login

GET    /api/boards
POST   /api/boards
GET    /api/boards/{id}
PUT    /api/boards/{id}
DELETE /api/boards/{id}

GET    /api/boards/{boardId}/lists
POST   /api/boards/{boardId}/lists

GET    /api/lists/{listId}/tasks
POST   /api/lists/{listId}/tasks
```

All endpoints except `/api/auth/**` require a valid JWT in the
`Authorization: Bearer <token>` header.

## Running locally

**Backend:**
```bash
cd taskflow-api
# set DB_URL, DB_USERNAME, DB_PASSWORD, JWT_SECRET in your environment
./mvnw spring-boot:run
```

**Frontend:**
```bash
cd taskflow-frontend
npm install
npm run dev
```

**Tests:**
```bash
cd taskflow-api
./mvnw test
```

## What I'd improve next

- Ownership checks on update/delete endpoints (currently strongest on create/list)
- Refresh tokens instead of a flat 24-hour JWT expiry
- Flyway migrations instead of `ddl-auto=update` for schema management
- Drag-and-drop task reordering on the frontend

## Screenshots

*(Add 2-3 screenshots here — board view, login screen, task creation)*
