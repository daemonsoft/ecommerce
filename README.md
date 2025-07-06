# Ecommerce Prices Service

[![Swagger UI](https://img.shields.io/badge/docs-Swagger%20UI-blue.svg)](http://localhost:8080/api/swagger-ui.html)

REST service to retrieve the applicable price for a product based on brand, date, and rate.

---

## 📚 Table of Contents

- [Requirements](#requirements)
- [How to run](#how-to-run)
- [How to test](#how-to-test)
- [How to format code](#how-to-format-code)
- [How to check format](#how-to-check-format)
- [API Endpoint](#api-endpoint)
- [Swagger / OpenAPI documentation](#swagger--openapi-documentation)
- [Technical notes](#technical-notes)
- [Request tracing & errors](#request-tracing--errors)

---

## Requirements
- Java 24
- Gradle

---

## How to run
```bash
./gradlew bootRun
````

---

## How to test

```bash
./gradlew test
```

---

## How to format code

This project uses [Spotless](https://github.com/diffplug/spotless) with Google Java Format.

Run:

```bash
./gradlew spotlessApply
```

---

## How to check format

```bash
./gradlew spotlessCheck
```

---

## API Endpoint

```
GET /api/v1/prices
```

---

### Query Parameters

| Name              | Type                           | Description                    |
| ----------------- | ------------------------------ | ------------------------------ |
| `brandId`         | Long                           | Brand ID                       |
| `productId`       | Long                           | Product ID                     |
| `applicationDate` | String (yyyy-MM-dd-HH\:mm\:ss) | Date & time in specific format |

---

## Example request

```bash
curl "http://localhost:8080/api/v1/prices?brandId=1&productId=35455&applicationDate=2020-06-14-10:00:00"
```

---

## Swagger / OpenAPI documentation

The project integrates with [springdoc-openapi](https://springdoc.org/) to automatically generate API documentation for WebFlux.

* OpenAPI JSON spec is available at:

  ```
  http://localhost:8080/api/v3/api-docs
  ```

* Swagger UI is available at:

  ```
  http://localhost:8080/api/swagger-ui.html
  ```

---

## Technical notes

* Uses an in-memory H2 database initialized at startup.
* Tests use JUnit 5 with Spring Boot `@SpringBootTest` and `WebTestClient`.
* OpenAPI annotations (`@Operation`, `@Parameter`) are used to document the REST endpoint.

---

## Request tracing & errors

Each request carries a `traceId` to correlate logs and errors.

* If the client sends an `X-Request-Id` header, it will be used.
* Otherwise, a new UUID is generated and logged.

All error responses follow a consistent format:

```json
{
  "timestamp": "2025-07-06T12:35:27.762Z",
  "status": 400,
  "message": "Invalid request: Invalid value 'xxx' for parameter 'applicationDate'. Type mismatch.",
  "traceId": "2ab2f623-c5b1-481d-b398-2ab095be4033"
}
```

This makes it easier to debug and trace requests across services.

---

## 🚀 Future Work

Here are some planned improvements and possible next steps for this project:

- **Dockerize the application:**  
  Package the service into a Docker container to simplify deployment and ensure consistent environments.

- **Publish a Docker image:**  
  Push the image to Docker Hub or GitHub Container Registry for easy consumption.

- **Native compilation:**  
  Explore building a native image with [GraalVM](https://www.graalvm.org/) to reduce startup time and memory footprint.
