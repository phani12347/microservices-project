# E-Commerce Microservices Backend

## Architecture

- API Gateway (Port 5000)
- Auth Service (Port 5001)
- Product Service (Port 5002)

Clients communicate only through API Gateway.

---

## Features

- User Registration & Login
- JWT Authentication
- Role-Based Access (ADMIN delete restriction)
- Product CRUD Operations
- MySQL Database
- Spring Cloud Gateway
- Spring Security
- JPA / Hibernate

---

## Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- JPA / Hibernate
- MySQL
- Maven

---

## How To Run

1. Start MySQL
2. Create database:

```sql
CREATE DATABASE microservices_db;
