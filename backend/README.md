# Backend - Online E-Commerce Shopping System

This directory contains the complete **Spring Boot REST API & Server Backend** for the Online E-Commerce Shopping System.

---

## 🏗️ Tech Stack
- **Framework:** Spring Boot 4.x / Java 21 LTS
- **Database:** H2 Embedded (In-memory zero-config) + MySQL 8.0 support
- **Persistence:** Spring Data JPA / Hibernate
- **Build Tool:** Apache Maven Wrapper

---

## 🚀 How to Run the Backend
Open a terminal in this `backend` folder and run:

```powershell
# Windows:
.\mvnw.cmd spring-boot:run
```

```bash
# Linux / macOS:
./mvnw spring-boot:run
```

The backend server will start on port `8080`:
- **Base URL:** `http://localhost:8080`
- **REST Endpoints:** `http://localhost:8080/api/products`, `http://localhost:8080/api/categories`, `http://localhost:8080/api/stats`, `http://localhost:8080/api/orders`
- **H2 Console:** `http://localhost:8080/h2-console`
