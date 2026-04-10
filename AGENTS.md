# Planning App - Agente Guidelines

## 1. Project Overview

Weekly planning application with task management organized by days of the week.

## 2. Tech Stack

### Backend
- **Java 21+** with **Spring Boot 4.0.5**
- **Spring Cloud** (Microservices architecture)
  - **Spring Cloud Gateway** - API Gateway with JWT validation
- **Spring Security** - Security layer
- **PostgreSQL** - Shared database
- **JPA/Hibernate** - ORM
- **JWT** - Authentication tokens
- **Docker Compose** - Container orchestration

### Frontend
- **Angular 21**
- **Manrope** font
- Custom design system (see DESIGN.md)

## 3. Architecture

```
Client → Gateway (:8080) → AuthService (:8081) / TaskService (:8082)
```

### Microservices
| Service | Port | Purpose |
|---------|------|---------|
| gateway | 8080 | Routing + JWT validation |
| auth-service | 8081 | Authentication (register/login) |
| task-service | 8082 | Task CRUD operations |

### Database
- **PostgreSQL** on port **5432** (shared across services)
- Database name: `planning_db`

## 4. Project Structure

```
planning/
├── DESIGN.md                 # Visual design system
├── AGENTS.md                 # This file
├── docker-compose.yml        # Container orchestration
├── gateway/                  # API Gateway
│   ├── src/main/java/
│   └── src/main/resources/
├── auth-service/             # Authentication service
│   ├── src/main/java/
│   └── src/main/resources/
└── task-service/            # Task management service
    ├── src/main/java/
    └── src/main/resources/
```

## 5. API Endpoints

### Auth Service (:8081)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/auth/register | Register new user |
| POST | /api/auth/login | Login and get JWT |

### Task Service (:8082)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /api/tasks | Get all user tasks |
| GET | /api/tasks/{id} | Get single task |
| POST | /api/tasks | Create task |
| PUT | /api/tasks/{id} | Update task |
| DELETE | /api/tasks/{id} | Delete task |
| PATCH | /api/tasks/{id}/complete | Toggle completion |

### Task Entity Schema
```json
{
  "id": "Long",
  "title": "String (required)",
  "description": "String",
  "dayOfWeek": "ENUM (LUNES-DOMINGO)",
  "time": "LocalTime",
  "completed": "Boolean (default: false)",
  "userId": "Long (from JWT)"
}
```

## 6. Security

### JWT Flow
1. User logs in via `/api/auth/login` → receives JWT
2. Client includes `Authorization: Bearer <token>` header
3. Gateway validates token with Spring Security
4. Valid requests forwarded to appropriate service

### JWT Payload
```json
{
  "sub": "user@email.com",
  "userId": 1,
  "iat": 1234567890,
  "exp": 1234571490
}
```

## 7. Design System (from DESIGN.md)

### Colors
| Token | Hex | Usage |
|-------|-----|-------|
| background | #f9f9f7 | Base canvas |
| surface-container-low | #f3f4f2 | Secondary areas |
| surface-container-lowest | #ffffff | Cards, inputs |
| surface-container-high | #e5e9e6 | Hover/active states |
| primary | #4f634f | Primary actions |
| primary-container | #d2e9cf | Chips, badges |
| on-surface | #2e3432 | Main text |
| on-surface-variant | #5a605e | Secondary text |
| tertiary | #506d5a | Metadata |
| outline | rgba(173,179,176,0.15) | Ghost borders |

### Typography
- Font: **Manrope** (Google Fonts)
- Scale: display-lg, display-md, display-sm, headline-lg, body-lg, label-md

### Component Rules
- **No solid borders** - Use color shifts instead
- **Border radius**: `xl` (1.5rem) for containers, `full` for buttons
- **Glassmorphism** for modals: 80% opacity + `backdrop-filter: blur(12px)`
- **No dividers** - Use vertical spacing (1.5rem)

## 8. Development Workflow

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Node.js 18+ (for frontend)

### Running Locally
1. Start Docker services: `docker-compose up -d postgres`
2. Start services in order:
   - `mvn spring-boot:run` in auth-service
   - `mvn spring-boot:run` in task-service
   - `mvn spring-boot:run` in gateway
3. Frontend: `ng serve` in frontend/

### Docker Compose
```bash
docker-compose up -d
```

## 9. Conventions

### Java
- Package naming: `com.planning.<service>.<module>`
- DTOs for API requests/responses
- JPA entities with `@Table` annotations
- Lombok for reducing boilerplate

### Git
- Branch: feature/<name>, fix/<name>
- Commit: conventional commits (feat:, fix:, docs:)
