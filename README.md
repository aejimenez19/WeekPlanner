# WeekPlanner

Weekly planning application with task management organized by days of the week.

## Tech Stack

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
- **Manrope** font (Google Fonts)
- Custom "Zen Editorial" design system

## Architecture

```
Client → Gateway (:8080) → AuthService (:8081) / TaskService (:8082)
```

### Microservices

| Service       | Port | Purpose                              |
|---------------|------|--------------------------------------|
| gateway       | 8080 | Routing + JWT validation             |
| auth-service  | 8081 | Authentication (register/login)     |
| task-service  | 8082 | Task CRUD operations                |

### Database
- **PostgreSQL** on port **5432**
- Database name: `planning_db`

## Features

- **Authentication**: Secure register/login with JWT tokens
- **Task Management**: Full CRUD operations for weekly tasks
  - Create, read, update, delete tasks
  - Toggle task completion
  - Organize tasks by day of the week (LUNES-DOMINGO)
- **Design System**: "Zen Editorial" aesthetic with:
  - Tonal color palette (Sage primary)
  - Glassmorphism for modals
  - No borders - color shifts only
  - Manrope typography

## Prerequisites

- **Java** 21+
- **Gradle** 9.4+
- **Docker** & **Docker Compose**
- **PostgreSQL** 14+ (or use Docker)
- **Node.js** 18+ (for frontend)
- **Angular CLI** 21+ (for frontend)

## Installation

### 1. Clone the repository

```bash
git clone git@github.com:aejimenez19/WeekPlanner.git
cd WeekPlanner
```

### 2. Start PostgreSQL with Docker

```bash
docker run -d \
  --name planning-db \
  -e POSTGRES_DB=planning_db \
  -e POSTGRES_USER=aejimenez \
  -e POSTGRES_PASSWORD=your_password \
  -p 5432:5432 \
  postgres:14
```

### 3. Backend Setup

Navigate to each service and run:

```bash
# Auth Service
cd back/auth-service
./gradlew bootRun

# Task Service (in another terminal)
cd back/task-service
./gradlew bootRun

# Gateway (in another terminal)
cd back/gateway
./gradlew bootRun
```

### 4. Frontend Setup

```bash
cd front
npm install
ng serve
```

## API Endpoints

### Auth Service (:8081)

| Method | Endpoint           | Description              |
|--------|-------------------|--------------------------|
| POST   | /api/auth/register | Register new user       |
| POST   | /api/auth/login   | Login and get JWT       |

### Task Service (:8082)

| Method | Endpoint              | Description                |
|--------|----------------------|----------------------------|
| GET    | /api/tasks            | Get all user tasks        |
| GET    | /api/tasks/{id}      | Get single task           |
| POST   | /api/tasks           | Create task               |
| PUT    | /api/tasks/{id}      | Update task               |
| DELETE | /api/tasks/{id}      | Delete task               |
| PATCH  | /api/tasks/{id}/complete | Toggle completion    |

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

## Security

### JWT Flow

1. User registers or logs in via `/api/auth/login` → receives JWT token
2. Client includes `Authorization: Bearer <token>` header in requests
3. Gateway validates token with Spring Security
4. Valid requests forwarded to appropriate service

### JWT Payload

```json
{
  "sub": "user@email.com",
  "userId": 1,
  "role": "USER",
  "iat": 1234567890,
  "exp": 1234571490
}
```

## Design System

### Color Palette

| Token                      | Hex       | Usage                      |
|----------------------------|-----------|----------------------------|
| background                 | #f9f9f7   | Base canvas                |
| surface-container-low      | #f3f4f2   | Secondary areas            |
| surface-container-lowest  | #ffffff   | Cards, inputs              |
| surface-container-high    | #e5e9e6   | Hover/active states        |
| primary                    | #4f634f   | Primary actions            |
| primary-container          | #d2e9cf   | Chips, badges              |
| on-surface                 | #2e3432   | Main text                  |
| on-surface-variant        | #5a605e   | Secondary text             |
| tertiary                   | #506d5a   | Metadata                   |

### Typography

- **Font**: Manrope (Google Fonts)
- **Scale**: display-lg, display-md, display-sm, headline-lg, body-lg, label-md

### Component Rules

- **No solid borders** - Use color shifts instead
- **Border radius**: `xl` (1.5rem) for containers, `full` for buttons
- **Glassmorphism** for modals: 80% opacity + `backdrop-filter: blur(12px)`
- **No dividers** - Use vertical spacing (1.5rem)

## Project Structure

```
WeekPlanner/
├── AGENTS.md                 # Developer guidelines
├── DESIGN.md                # Design system documentation
├── .gitignore
├── README.md
├── back/
│   ├── auth-service/        # Authentication service
│   ├── task-service/       # Task management service
│   └── gateway/            # API Gateway
└── front/                  # Angular frontend
```

## Contributing

### Git Conventions

- **Branches**: `feature/<name>`, `fix/<name>`
- **Commits**: Conventional commits
  - `feat:` - New feature
  - `fix:` - Bug fix
  - `docs:` - Documentation
  - `refactor:` - Code refactoring
  - `test:` - Tests

### Code Standards

- **Java**: Lombok for boilerplate reduction, DTOs for API, JPA entities with `@Table`
- **Packages**: `com.aejimenezdev.<service>.<module>`

## License

MIT License