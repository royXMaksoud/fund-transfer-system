# 💸 Fund Transfer Platform – Microservices System  !
<pre> ``` fund-transfer-service/ ├── application.service/ │ ├── locking/ │ ├── config/ ├── domain/ │ ├── model/ │ ├── ports/ │ ├── in/ │ └── out/ ├── infrastructure/ │ ├── db/ │ ├── persistence.adapters/ │ └── security/ ├── shared/ │ ├── constants/ │ └── exception/ ├── web/ │ ├── controller/ │ ├── dto/ │ └── mappers/ ``` </pre>


This project is a secure, scalable, and concurrent microservice-based system for managing user authentication and money transfers between accounts. It follows **Clean Architecture**, **JWT Security**, and **Spring Cloud Gateway** for routing and API composition.

---

## 🧱 Architecture Overview

The platform consists of **three core services**:

```
fund-transfer-platform/
├── auth-service/            # Handles authentication, JWT, refresh tokens
├── fund-transfer-service/   # Handles secure fund transfers between accounts
├── gateway-service/         # Routes and secures access to the services
```

Each service is containerized and communicates using REST APIs. JWT tokens are used for inter-service authentication.

---

## 📌 Services Summary

### 🔐 Auth Service
Handles user registration, login, JWT token generation, refresh token management, and role-based access control.

#### Features:
- User registration and login with hashed passwords
- JWT token issuance with `username` and `role` claims
- Refresh token support with DB persistence and expiry
- Account lockout after multiple failed login attempts
- Global exception handling via `@RestControllerAdvice`
- Swagger UI for easy testing

#### 🔗 Endpoints:
| Method | Path                 | Description                    | Auth Required |
|--------|----------------------|--------------------------------|---------------|
| POST   | `/auth/register`     | Register a new user            | ❌            |
| POST   | `/auth/login`        | Login and receive JWT+refresh  | ❌            |
| POST   | `/auth/refresh`      | Refresh JWT token              | ✅            |
| GET    | `/auth/user/profile` | Get current user profile       | ✅            |

---

### 🏦 Fund Transfer Service
Responsible for transferring funds between accounts with concurrency control via locking strategies.

#### Features:
- In-memory and database locking for concurrent transfers
- Transfer history fetching by account ID
- Full integration test suite for business logic & security
- DTO-based communication and Swagger/OpenAPI documentation

#### Locking Strategies:
| Strategy     | Description                                | Scope         |
|--------------|--------------------------------------------|---------------|
| MEMORY       | Uses `ReentrantLock` for local safety      | Single Node   |
| DATABASE     | Uses JPA `PESSIMISTIC_WRITE` for DB lock   | Multi-Node    |

```yaml
ftp:
  lock-strategy: MEMORY  # or DATABASE
```

#### 🔐 Security:
All endpoints are secured via JWT. Tokens must be obtained from `auth-service` and sent via `Authorization: Bearer <token>`.

---

### 🌐 Gateway Service
Acts as the single entry point to the platform. Built with **Spring Cloud Gateway + WebFlux**.

#### Features:
- Routes requests to `auth-service` and `fund-transfer-service`
- Forwards JWT headers to downstream services
- Serves aggregated Swagger UI docs
- Path rewriting for `/swagger-ui` and `/v3/api-docs`

#### Sample Routes:
| Route Prefix | Destination Service    |
|--------------|------------------------|
| `/auth/**`   | `auth-service:8080`     |
| `/transfers/**` | `fund-transfer-service:8081` |

---

## 🔧 Tech Stack

| Layer         | Technology                                |
|---------------|-------------------------------------------|
| Language      | Java 17                                   |
| Framework     | Spring Boot 3.x, Spring Cloud Gateway     |
| Security      | Spring Security, JWT                      |
| Persistence   | PostgreSQL, Spring Data JPA, Liquibase    |
| Docs          | Swagger UI, SpringDoc OpenAPI             |
| Architecture  | Clean Architecture (Ports & Adapters)     |
| API Tests     | JUnit, MockMvc, SpringBootTest            |

---

## 📂 Configuration Examples

### `auth-service/src/main/resources/application.yml`

```yaml
server.port: 8080

jwt:
  secret: VerySecureSecretKey...
  expiration: 86400000

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/fund_transfer_db
    username: postgres
    password: P@ssw0rd
  jpa:
    hibernate.ddl-auto: update
  doc:
    swagger-ui.path: /swagger-ui.html
```

### `gateway-service/src/main/resources/application.yml`

```yaml
server.port: 9090

spring.cloud.gateway.routes:
  - id: auth-service
    uri: http://localhost:8080
    predicates:
      - Path=/auth/**
  - id: fund-transfer-service
    uri: http://localhost:8081
    predicates:
      - Path=/transfers/**
```

---

## 🧪 Testing Strategy

- ✅ Unit tests for business logic (login, transfer, JWT)
- ✅ Integration tests with real DB & JWT
- ✅ Concurrency test using `ExecutorService`
- ✅ Swagger UI for manual API exploration

---

## 🚀 Getting Started

1. Start PostgreSQL DB (`fund_transfer_db`)
2. Run all three services
3. Access Swagger UI via:
   ```
   http://localhost:9090/webjars/swagger-ui/index.html
   ```

---

## ✅ Status

| Component            | Status     |
|----------------------|------------|
| Auth Service         | ✅ Complete |
| Fund Transfer Service| ✅ Complete |
| Gateway Service      | ✅ Complete |
| Swagger Integration  | ✅ Ready    |
| JWT Secured Endpoints| ✅ Enabled  |

---

## 👨‍💻 Author

Designed and implemented by a passionate developer for a **Java Developer Technical Interview** task.

---

## 📄 License

This project is provided for educational and technical evaluation purposes.