# Ecommerce Prices Service

[![Swagger UI](https://img.shields.io/badge/docs-Swagger%20UI-blue.svg)](http://localhost:8080/api/swagger-ui.html)
[![Java](https://img.shields.io/badge/Java-24-orange.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.14.2-blue.svg)](https://gradle.org/)

REST service to retrieve the applicable price for a product based on brand, date, and priority.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Configuration](#configuration)
- [Development](#development)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

---

## Overview

This service implements a price retrieval system for an ecommerce platform. It follows **Hexagonal Architecture (Ports & Adapters)** principles and implements **SOLID** design principles to ensure maintainability and scalability.

### Key Features

- **Reactive Programming**: Built with Spring WebFlux for non-blocking I/O
- **Hexagonal Architecture**: Clean separation between domain, application, and infrastructure layers
- **Database Optimization**: Efficient queries with optimized indexing
- **Comprehensive Error Handling**: Structured error responses with request tracing
- **API Documentation**: Auto-generated OpenAPI/Swagger documentation
- **Request Tracing**: Built-in trace ID correlation for debugging

---

## Architecture

### Hexagonal Architecture Implementation

The application follows the **Hexagonal Architecture** pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Infrastructure Layer                     │
├─────────────────────────────────────────────────────────────┤
│  Controllers │  Persistence │  Configuration │  External    │
│              │              │                │  Services    │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                    Application Layer                        │
├─────────────────────────────────────────────────────────────┤
│  Use Cases   │  DTOs        │  Mappers      │  Services     │
└─────────────────────────────────────────────────────────────┘
                              │
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                           │
├─────────────────────────────────────────────────────────────┤
│  Entities    │  Repositories│  Exceptions   │  Value        │
│              │  (Ports)     │               │  Objects      │
└─────────────────────────────────────────────────────────────┘
```

### Package Structure

```
src/main/java/com/daemonsoft/ecommerce/v1/
├── config/                    # Configuration classes
├── prices/
│   ├── application/          # Application services (use cases)
│   ├── domain/              # Domain entities and business logic
│   └── infrastructure/      # Infrastructure adapters
│       └── persistence/     # Database table mappings and adapters
```
---

## Technology Stack

### Core Framework
- **Java 24**: Latest LTS version with modern language features
- **Spring Boot 3.5.3**: Rapid application development framework
- **Spring WebFlux**: Reactive web framework for non-blocking I/O
- **Spring Data R2DBC**: Reactive database connectivity

### Database & Persistence
- **H2 Database**: In-memory database for development and testing
- **R2DBC**: Reactive database connectivity
- **Spring Data**: Data access abstraction layer

### Testing
- **JUnit 5**: Modern testing framework
- **Mockito**: Mocking framework for unit tests
- **Spring Boot Test**: Integration testing support
- **WebTestClient**: Reactive web testing client
- **AssertJ**: Fluent assertion library

### Documentation & API
- **OpenAPI 3**: API specification standard
- **SpringDoc**: Automatic API documentation generation
- **Swagger UI**: Interactive API documentation

### Build & Quality
- **Gradle**: Build automation tool
- **Spotless**: Code formatting with Google Java Format
- **Spring Boot DevTools**: Development convenience features

### Monitoring & Observability
- **SLF4J**: Logging facade
- **Request Tracing**: Custom trace ID implementation
- **Structured Error Responses**: Consistent error handling

---

## Requirements

- **Java 24** or higher
- **Gradle 8.14.2** or higher
- **Minimum 512MB RAM** for development
- **Internet connection** for dependency download

---

## Quick Start

### Prerequisites

1. Install Java 24:
   ```bash
   # Using SDKMAN (recommended)
   sdk install java 24.0.1-graal
   sdk use java 24.0.1-graal

   # Or download from Oracle/OpenJDK https://jdk.java.net/24/
   ```

2. Verify installation:
   ```bash
   java --version
   gradle --version
   ```

### Clone and Setup

```bash
git clone https://github.com/daemonsoft/ecommerce
cd ecommerce
./gradlew build
```

### Run the Application

```bash
# Development mode
./gradlew bootRun

# Or build and run JAR
./gradlew build
java -jar build/libs/ecommerce-0.0.1-SNAPSHOT.jar
```

### Test the API

```bash
# Test the price endpoint
curl "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&applicationDate=2020-06-14-10:00:00"
```

### Access Documentation

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/v3/api-docs

---

## API Documentation

### API Endpoint

```
GET /api/v1/prices
```

### Query Parameters

| Name              | Type                           | Description                    | Required |
| ----------------- | ------------------------------ | ------------------------------ | -------- |
| `brandId`         | Long                           | Brand ID                       | Yes      |
| `productId`       | Long                           | Product ID                     | Yes      |
| `applicationDate` | String (yyyy-MM-dd-HH:mm:ss)   | Date & time in specific format | Yes      |

### Example Request

```bash
curl "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&applicationDate=2020-06-14-10:00:00"
```

### Response Format

```json
{
  "brandId": 1,
  "productId": 35455,
  "rateId": 2,
  "finalPrice": 25.45,
  "currency": "EUR",
  "applicationDate": "2020-06-14-10:00:00"
}
```

### Error Response Format

```json
{
  "timestamp": "2025-07-06T12:35:27.762Z",
  "status": 400,
  "message": "Invalid request: Invalid value 'xxx' for parameter 'applicationDate'",
  "traceId": "2ab2f623-c5b1-481d-b398-2ab095be4033"
}
```

---

## Configuration

### Environment-Specific Configuration

The application supports different configuration profiles:

#### Development (default)
```yaml
spring:
  r2dbc:
    url: r2dbc:h2:mem:///ecommerce_db
  sql:
    init:
      mode: always
logging:
  level:
    org.springframework.r2dbc: DEBUG
```

#### Production
```yaml
spring:
  r2dbc:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}
logging:
  level:
    root: WARN
    com.daemonsoft.ecommerce: INFO
```

### Configuration Properties

| Property | Description | Default | Environment |
|----------|-------------|---------|-------------|
| `spring.r2dbc.url` | Database connection URL | `r2dbc:h2:mem:///ecommerce_db` | All |
| `spring.webflux.base-path` | API base path | `/api` | All |
| `logging.level.root` | Root logging level | `INFO` | All |

---

## Development

### Code Formatting

This project uses [Spotless](https://github.com/diffplug/spotless) with Google Java Format.

```bash
# Format code
./gradlew spotlessApply

# Check formatting
./gradlew spotlessCheck
```

### Database Schema

The application uses an optimized database schema with proper indexing:

```sql
CREATE TABLE prices (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id      BIGINT         NOT NULL,
    valid_from    TIMESTAMP      NOT NULL,
    valid_until   TIMESTAMP      NOT NULL,
    rate_id       BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    priority      INT            NOT NULL,
    final_price   DECIMAL(10, 2) NOT NULL,
    currency_code VARCHAR(3)     NOT NULL
);

-- Optimized index for price queries
CREATE INDEX idx_prices_product_brand_dates
    ON prices (product_id, brand_id, priority DESC, valid_from, valid_until);
```

### Business Logic

The service implements the following business rules:

1. **Price Priority**: Higher priority prices override lower priority ones
2. **Date Range Validation**: Prices are only valid within their `valid_from` and `valid_until` range
3. **Single Result**: Returns the highest priority price for the given criteria
4. **Error Handling**: Comprehensive validation and error responses

---

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Categories
```bash
# All tests (default)
./gradlew test

# Unit tests only
./gradlew test --tests "PriceTest"

# Integration tests only
./gradlew test --tests "*IntegrationTest"

# Application service tests
./gradlew test --tests "*GetHighestPriorityPriceTest*"

```

### Test Structure

- **Unit Tests**: Test individual components in isolation
- **Integration Tests**: Test component interactions

---

## Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Check what's using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

#### Database Connection Issues
- Verify H2 database is starting correctly
- Check application logs for connection errors
- Ensure database schema is being initialized

#### Test Failures
- Run `./gradlew clean test` to ensure clean state
- Check test logs for specific error messages
- Verify test data in `data.sql` is correct

### Logs and Debugging

#### Enable Debug Logging
```yaml
logging:
  level:
    com.daemonsoft.ecommerce: DEBUG
    org.springframework.r2dbc: DEBUG
```

#### Request Tracing
Each request includes a `traceId` for correlation:
- Client-provided: `X-Request-Id` header
- Auto-generated: UUID if not provided

### Performance Monitoring

The application includes:
- **Request/Response Logging**: With trace IDs for correlation
- **Database Query Logging**: For performance analysis
- **Error Tracking**: Structured error responses with context

---

## 🚀 Future Enhancements

### Performance Optimizations
- **Caching Layer**: Implement Redis for price caching
- **Database Optimization**: Connection pooling and query optimization
- **Response Compression**: Gzip compression for API responses

### Scalability Improvements
- **Horizontal Scaling**: Load balancer configuration

### Monitoring & Observability
- **Metrics**: Prometheus metrics integration
- **Distributed Tracing**: Jaeger/Zipkin integration
- **Health Checks**: Comprehensive health endpoints

### DevOps & Deployment
- **Docker Support**: Multi-stage Docker builds
- **CI/CD Pipeline**: GitHub Actions or Jenkins

### Security Enhancements
- **Authentication**: JWT-based authentication
- **Authorization**: Role-based access control
- **API Rate Limiting**: Request throttling
- **Input Validation**: Enhanced validation rules

### Testing Improvements
- **Performance Testing**: Gatling load tests
- **Mutation Testing**: PIT for test quality
- **Security Testing**: OWASP dependency check
