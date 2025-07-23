# Gateway Service

This is the API Gateway for the Fund Transfer Platform, built using Spring Cloud Gateway and WebFlux.  
It routes external HTTP requests to internal microservices such as `auth-service` and `fund-transfer-service`,  
and handles JWT propagation, Swagger documentation access, and request logging.

---

## 🧱 Features

- Static routing to internal microservices
- JWT Authorization header forwarding
- Swagger UI aggregation for all services
- Logging filter for tracking requests

---

## 🚀 How It Works

The gateway receives requests on port `9090` and forwards them to the correct microservice based on the path prefix:

| Route Prefix     | Forwarded To            | Example                              |
|------------------|--------------------------|--------------------------------------|
| `/auth/**`       | `http://localhost:8080`  | `/auth/login` → `auth-service`       |
| `/transfers/**`  | `http://localhost:8081`  | `/transfers` → `fund-transfer-service` |

Swagger documentation is also available and mapped via path rewriting.

---

## ⚙️ Configuration

### `application.yml`
```yaml
server:
  port: 9090

spring:
  cloud:
    gateway:
      default-filters:
        - PreserveHostHeader
      routes:
        - id: auth-service
          uri: http://localhost:8080
          predicates:
            - Path=/auth/**
          filters:
            - RewritePath=/auth/v3/api-docs(?<segment>/.*), /v3/api-docs${segment}
            - RewritePath=/auth/v3/api-docs, /v3/api-docs
            - RewritePath=/auth/swagger-ui(?<segment>/.*), /swagger-ui${segment}
            - RewritePath=/auth/swagger-ui.html, /swagger-ui.html
            - name: Logging

        - id: fund-transfer-service
          uri: http://localhost:8081
          predicates:
            - Path=/transfers/**
          filters:
            - RewritePath=/transfers/v3/api-docs(?<segment>/.*), /v3/api-docs${segment}
            - RewritePath=/transfers/v3/api-docs, /v3/api-docs
            - RewritePath=/transfers/swagger-ui(?<segment>/.*), /swagger-ui${segment}
            - RewritePath=/transfers/swagger-ui.html, /swagger-ui.html
            - name: Logging

springdoc:
  swagger-ui:
    path: /swagger-ui.html
    urls:
      - name: auth-service
        url: /auth/v3/api-docs
      - name: fund-transfer-service
        url: /transfers/v3/api-docs
```

---

## 🔐 JWT Support

- JWT tokens are expected to be included in the `Authorization` header:  
  `Authorization: Bearer <your_token>`
- Tokens are forwarded to services downstream.

---

## 📘 Swagger UI

Swagger UI for all services is accessible via:
```
http://localhost:9090/webjars/swagger-ui/index.html
```

---

## 🛠️ Technologies

- Java 17
- Spring Boot 3.5.3
- Spring Cloud Gateway (2025.0.0)
- Spring WebFlux
- Springdoc OpenAPI

---

## 📂 Structure

```
gateway-service/
├── application.yml
├── GatewayApplication.java
└── (other filters/configs)
```

---

## 🧪 Run & Test

1. Start all microservices (`auth-service`, `fund-transfer-service`)
2. Run this service on port 9090.
3. Test Swagger at: `http://localhost:9090/webjars/swagger-ui/index.html`

---

## ✅ Status

✅ Static routing working  
✅ JWT forwarding working  
✅ Swagger UI integrated