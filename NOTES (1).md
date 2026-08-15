<!-- # TaskFlow — Interview Notes

## Q1: Walk me through what happens when a user creates a new task, from click to saved.

"On the frontend, the user submits a form. Axios sends a `POST` request to
`/api/lists/{listId}/tasks` with a JWT in the `Authorization` header and the
task title in the JSON body.

On the backend, `JwtAuthFilter` intercepts the request first — before it even
reaches my controller. It reads the token, validates the signature, looks up
the user by the email stored in the token's claims, and places that user into
Spring Security's context.

The request then hits `TaskController`, which is deliberately thin — it just
extracts the `listId` from the URL and the title from the request body, and
delegates to `TaskService.createTask()`.

`TaskService` does the actual logic: it looks up the parent `TaskList` by ID
using `TaskListRepository`, and if that list doesn't exist, throws a custom
`TaskListNotFoundException` — which `GlobalExceptionHandler` catches and turns
into a clean `404` JSON response instead of a stack trace. If the list does
exist, it builds a new `Task` entity, sets its status to `TODO`, links it to
the parent list, and calls `taskRepository.save()`.

Spring Data JPA translates that `save()` call into a real `INSERT` SQL
statement against Postgres — I never write that SQL myself, Hibernate
generates it from the entity's `@Entity`/`@ManyToOne` annotations.

The saved entity flows back up through the Service to the Controller, which
returns it — Spring's Jackson library serializes it to JSON automatically
because the class is annotated `@RestController`. The frontend receives the
JSON, updates its React state, and the new task appears on screen without a
page reload."

---

## Q2: Why split Controller / Service / Repository into three layers?

"Single responsibility. Each layer has exactly one job:

- **Controller** — HTTP concerns only: routes, status codes, request/response
  shape. It shouldn't know anything about business rules.
- **Service** — business logic: validation, orchestration across multiple
  repositories, deciding what's allowed.
- **Repository** — data access only, via Spring Data JPA interfaces.

The real payoff shows up when something changes. When I moved from an
in-memory `List<Board>` to real PostgreSQL with Spring Data JPA, my
Controllers didn't change at all, and my Services barely changed — only the
Repository layer was swapped. If I'd wired Repository calls directly into
Controllers, that migration would have meant rewriting every endpoint.

It also makes testing possible — my unit tests mock the Repository layer with
Mockito, so I can test Service logic in milliseconds with zero database
dependency."

---

## Q3: How do you make sure User A can't see User B's boards?

"Ownership is enforced at two points.

First, at write time: when a board is created, `BoardService.createBoard()`
takes the currently authenticated user (pulled from
`@AuthenticationPrincipal`, which comes from the JWT — never trusted from a
client-supplied parameter) and sets it as the board's owner via the
`@ManyToOne User user` relationship.

Second, at read time: `getBoardsForUser()` calls
`boardRepository.findByUserId(currentUser.getId())` — a derived query method
that filters at the database level, so a user's query can only ever return
rows they own. The identity comes from the verified JWT, not from anything
the client sends, so there's no way to spoof another user's ID and see their
data."

---

## Q4: What happens if someone sends a POST with an empty title?

"The request hits `@Valid @RequestBody CreateBoardRequest request` in the
controller. The DTO has `@NotBlank` and `@Size(max = 100)` on the `title`
field. Before my controller method body even runs, Spring validates the
incoming JSON against those annotations.

If it fails, Spring throws `MethodArgumentNotValidException`, which never
reaches my business logic — `GlobalExceptionHandler`'s
`handleValidationErrors()` catches it and returns a `400 Bad Request` with a
field-specific error map, like `{"title": "Title is required"}`. That's
exactly the shape a frontend needs to show an inline form error.

I validate on the DTO, not the entity, because DTOs represent what a client
is allowed to send — the entity represents what's actually stored, and those
aren't always the same shape."

---

## Q5: How does your app know a request is from a logged-in user?

"Token-based auth, not sessions. On login, `AuthService` verifies the
password with BCrypt (`passwordEncoder.matches()`) and issues a signed JWT
containing the user's email as the subject claim.

Every subsequent request must include that token in the `Authorization:
Bearer <token>` header. `JwtAuthFilter`, which runs once per request before
it reaches any controller, extracts the token, verifies its signature against
my secret key, and — if valid — looks up the user and places them in
`SecurityContextHolder`.

My `SecurityConfig` sets session policy to `STATELESS`, meaning the server
never remembers who you are between requests — every single request has to
prove it via the token. That's what makes this safely scalable and consistent
with how real production APIs are built, versus older cookie/session-based
auth."

---

## The one meta-answer worth memorizing

If asked "what would you improve about this project":

"Right now ownership checks happen mostly on create and list — I'd add
explicit checks on update/delete too, so a user can't guess another user's
board ID and modify it directly. I'd also add refresh tokens instead of a
flat 24-hour JWT expiry, and move from `ddl-auto=update` to a real migration
tool like Flyway for schema changes in production."

This answer is good because it shows self-awareness without undermining
confidence in what you built — a strong signal in interviews. -->
