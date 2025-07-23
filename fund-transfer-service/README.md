
# 🏦 Fund Transfer Service – Locking Strategy & Database Guide

This microservice handles money transfers between accounts using a robust locking mechanism to prevent race conditions and ensure consistency under concurrent requests.

---

## 🔐 Locking Strategies Supported

### 1. In-Memory Locking (`ReentrantLock`)
- **Description:** Uses Java's built-in `ReentrantLock` to prevent concurrent transfers on the same sender account.
- **Scope:** Only works within a single instance of the application.
- **Advantages:** Fast and lightweight.
- **Limitations:** Not safe in distributed/multi-instance environments.

### 2. Database Locking (`PESSIMISTIC_WRITE`)
- **Description:** Uses JPA `PESSIMISTIC_WRITE` locking to acquire a DB-level lock on the sender's row during the transfer.
- **Scope:** Safe across multiple instances.
- **Advantages:** Guarantees correctness even with concurrent requests across different pods.
- **Limitations:** Slightly slower due to DB locking overhead.

---

## 🔄 Switching Locking Strategies

You can toggle between locking strategies using the `application.yml` file:

```yaml
ftp:
  lock-strategy: MEMORY # or DATABASE
```

- Use `MEMORY` for in-memory locking (good for development or single-instance).
- Use `DATABASE` for distributed, production-safe locking.

---

## 🧪 Integration Tests Coverage

| Test Class                           | Description                                                  |
|--------------------------------------|--------------------------------------------------------------|
| `SuccessfulTransferIntegrationTest`  | Verifies correct transfer behavior                           |
| `InsufficientBalanceIntegrationTest` | Verifies failure on low balance                              |
| `InvalidTransferRequestTest`         | Validates required fields and input correctness              |
| `ConcurrentTransferIntegrationTest`  | Simulates multiple threads transferring at the same time     |
| `LockStrategySwitchIntegrationTest`  | Ensures strategy switch affects behavior as expected         |
| `GetTransfersIntegrationTest`        | Verifies fetching transfer history by account ID             |
| `SecurityIntegrationTest`           | Verifies JWT is required and unauthorized access is blocked  |
| `TransferControllerTest`            | Covers controller logic with mock service                    |

---

## 🧰 Database Configuration

### ✅ Default Database – PostgreSQL

- Used for all environments including integration tests.
- Schema managed via **Liquibase**.
- To run with PostgreSQL, make sure your `application.yml` contains:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your-db
    username: your-username
    password: your-password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
```

> 💡 This configuration is ideal for production or shared development environments.

### 🧪 Temporary Database for Tests – H2

H2 can be used locally to inspect data during testing or rapid prototyping.

#### Activate H2 Profile

Update your run configuration and set active profile to `test` or use:
```bash
-Dspring.profiles.active=test
```

#### Sample H2 Configuration (in `application-test.yml`)

```yaml
spring:
  datasource:
    url: jdbc:h2:file:./data/testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
    driver-class-name: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.H2Dialect
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8081
```

#### Accessing H2 Console

1. Run the service with the `test` profile.
2. Navigate to: [http://localhost:8081/h2-console](http://localhost:8081/h2-console)
3. Use the following credentials:
   - JDBC URL: `jdbc:h2:file:./data/testdb`
   - Username: `sa`
   - Password: *(leave blank)*

---

## 📄 Swagger API Docs

Once the service is running, access Swagger UI via:

```
http://localhost:8081/swagger-ui.html
```

If using Spring Cloud Gateway, the Swagger UI should be available at:

```
http://localhost:9090/swagger-ui/fund-transfer
```

> Requires correct route configuration in the gateway.

---

## 🔐 Security Notes

- All transfer endpoints are secured with JWT.
- Token must be obtained from the `auth-service` before accessing protected endpoints.

---

## ▶️ Running Tests

To run all tests:

```bash
./mvnw clean test
```

Or using IntelliJ:

- Right-click the `test` folder → Run All Tests

> 💡 Make sure PostgreSQL is running if tests use it. For H2, ensure the `test` profile is activated.

---

## 🔄 Performance Testing with Concurrent Transfers

### Test Description

This test simulates concurrent transfer requests to ensure the system handles multiple simultaneous transactions. It also verifies that the locking mechanism (in-memory and DB) works as expected.

#### Key Components:

- **ExecutorService:** Handles concurrent execution of transfer requests.
- **CountDownLatch:** Ensures that we wait for all threads to complete before finishing the test.
- **MockMvc:** Simulates HTTP requests to the transfer API.

## 🧪 Concurrent Transfer Handling Test

This test simulates concurrent transfer requests to ensure that the system correctly handles multiple simultaneous transactions. It verifies that the locking mechanism (either in-memory or DB-based) works as expected and prevents race conditions.

### Key Points:
- **Concurrency Handling:** The test simulates 10 concurrent transfers between two accounts.
- **Locking Mechanism:** It verifies that the in-memory (`ReentrantLock`) and database-level (`PESSIMISTIC_WRITE`) locking strategies function correctly under load.
- **JWT Authentication:** The test uses a valid JWT token to authenticate each transfer request.

### Test Logic:
1. **Multiple Threads:** The test creates 10 threads to execute transfers concurrently.
2. **Transfer Request:** Each thread creates a transfer request with a specified amount and currency.
3. **Assertions:** The test ensures each transfer completes successfully and returns a status of 200 OK.

### Where to Find the Code:
The full implementation of this test can be found in `src/test/java/com/ftp/fundtransferservice/PerformanceIntegrationTest.java`.

---

## 🚀 Further Steps

- Continue monitoring and optimizing the performance under concurrent load.
- Review the integration tests to ensure they cover all edge cases.

---


