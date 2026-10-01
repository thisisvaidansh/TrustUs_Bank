# 🏦 TrustUs Bank

A full-stack banking application with secure **Deposit**, **Withdrawal**, and **Transfer** APIs, **JWT-based authentication** between a React frontend and a Spring Boot backend, **AWS S3** file storage, and **SMTP email alerts** for transactions and security events. The backend API is containerized with Docker.

> This repository contains the **Spring Boot backend API**. The React frontend is a separate client that consumes this API.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1)
![JWT](https://img.shields.io/badge/Auth-JWT-black)
![AWS S3](https://img.shields.io/badge/AWS-S3-FF9900)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED)

---

## 📑 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [Configuration](#-configuration)
- [Running with Docker](#-running-with-docker)
- [API Overview](#-api-overview)
- [Authentication Flow](#-authentication-flow)
- [Email Notifications](#-email-notifications)
- [AWS S3 File Storage](#-aws-s3-file-storage)
- [Testing](#-testing)
- [Security Notes](#-security-notes)
- [Authors](#-authors)

---

## ✨ Features

- **Secure banking operations** – Deposit, withdraw, and transfer funds between accounts.
- **JWT authentication** – Stateless token-based auth using Spring Security and JJWT.
- **Input validation** – Request validation via Jakarta Bean Validation.
- **AWS S3 integration** – Store and retrieve files using the AWS SDK for Java v2.
- **Email alerts** – Transaction and security notifications sent over SMTP.
- **Dockerized API** – Consistent builds and deployment with Docker.
- **Clean DTO mapping** – Entity ↔ DTO conversion with ModelMapper.

---

## 🛠 Tech Stack

| Layer        | Technology                                                   |
| ------------ | ------------------------------------------------------------ |
| Language     | Java 21                                                      |
| Framework    | Spring Boot 4.1.0 (Web MVC, Data JPA, Security, Validation)  |
| Auth         | Spring Security + JJWT 0.13.0                                |
| Database     | MySQL (via `mysql-connector-j`)                              |
| Storage      | AWS S3 (AWS SDK v2 `s3` 2.46.x)                              |
| Email        | Spring Boot Starter Mail (SMTP)                              |
| Templating   | Thymeleaf (with Spring Security extras)                      |
| Utilities    | Lombok, ModelMapper                                          |
| Build        | Maven (Maven Wrapper included)                               |
| DevOps       | Docker                                                       |
| Frontend     | React (separate client)                                      |

---

## 🏗 Architecture

```
┌──────────────┐     HTTP + JWT (Bearer)     ┌─────────────────────┐
│   React UI   │ ──────────────────────────▶ │   Spring Boot API   │
│              │ ◀────────────────────────── │     (Docker)        │
└──────────────┘        JSON responses       └──────────┬──────────┘
                                                        │
                    ┌───────────────────────────────────┼──────────────────────────┐
                    ▼                                   ▼                          ▼
             ┌─────────────┐                    ┌──────────────┐           ┌──────────────┐
             │    MySQL    │                    │   AWS S3     │           │ SMTP Server  │
             │ (accounts,  │                    │ (file store) │           │ (email alerts)│
             │ transactions)│                   └──────────────┘           └──────────────┘
             └─────────────┘
```

---

## 📋 Prerequisites

- **Java 21** (JDK)
- **MySQL 8+** running locally or remotely
- **Docker** (optional, for containerized runs)
- An **AWS account** with an S3 bucket and IAM credentials
- An **SMTP account** (Gmail App Password, Mailtrap, SendGrid, etc.)

Maven is **not** required to be installed – the included Maven Wrapper (`mvnw`) is used.

---

## 🚀 Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/thisisvaidansh/TrustUs_Bank.git
cd TrustUs_Bank
```

### 2. Create the database

```sql
CREATE DATABASE trustus_bank;
```

### 3. Configure the application

Set your properties in `src/main/resources/application.properties` (see [Configuration](#-configuration)), or provide them as environment variables.

### 4. Build and run

**Linux / macOS**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

**Windows**
```bash
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8080` by default.

---

## ⚙️ Configuration

Example `application.properties` (keep real secrets out of version control):

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/trustus_bank
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=${JWT_SECRET}
jwt.expiration-ms=3600000

# SMTP
spring.mail.host=${SMTP_HOST}
spring.mail.port=${SMTP_PORT}
spring.mail.username=${SMTP_USERNAME}
spring.mail.password=${SMTP_PASSWORD}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# AWS S3
aws.access-key=${AWS_ACCESS_KEY_ID}
aws.secret-key=${AWS_SECRET_ACCESS_KEY}
aws.region=${AWS_REGION}
aws.s3.bucket=${AWS_S3_BUCKET}
```

> The property names above are examples – match them to the keys your code actually reads.

### Environment variables

| Variable                | Description                              |
| ----------------------- | ---------------------------------------- |
| `DB_USERNAME`           | MySQL username                           |
| `DB_PASSWORD`           | MySQL password                           |
| `JWT_SECRET`            | Secret used to sign JWTs (256-bit+)      |
| `SMTP_HOST`             | SMTP host (e.g. `smtp.gmail.com`)        |
| `SMTP_PORT`             | SMTP port (e.g. `587`)                   |
| `SMTP_USERNAME`         | SMTP username / sender email             |
| `SMTP_PASSWORD`         | SMTP password / app password             |
| `AWS_ACCESS_KEY_ID`     | AWS IAM access key                       |
| `AWS_SECRET_ACCESS_KEY` | AWS IAM secret key                       |
| `AWS_REGION`            | AWS region (e.g. `ap-south-1`)           |
| `AWS_S3_BUCKET`         | S3 bucket name                           |

⚠️ **Never commit credentials.** Use environment variables or a git-ignored properties file.

---

## 🐳 Running with Docker

Example multi-stage `Dockerfile` for the API:

```dockerfile
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t trustus-bank-api .
docker run -p 8080:8080 --env-file .env trustus-bank-api
```

### With Docker Compose (API + MySQL)

```yaml
services:
  db:
    image: mysql:8
    environment:
      MYSQL_DATABASE: trustus_bank
      MYSQL_ROOT_PASSWORD: ${DB_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - db_data:/var/lib/mysql

  api:
    build: .
    ports:
      - "8080:8080"
    env_file: .env
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/trustus_bank
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: ${DB_PASSWORD}
    depends_on:
      - db

volumes:
  db_data:
```

```bash
docker compose up --build
```

---

## 📡 API Overview

All protected endpoints require the header:

```
Authorization: Bearer <JWT>
```

| Method | Endpoint (example)         | Description                         | Auth |
| ------ | -------------------------- | ----------------------------------- | ---- |
| POST   | `/api/auth/register`       | Register a new user                 | ❌   |
| POST   | `/api/auth/login`          | Authenticate and receive a JWT      | ❌   |
| POST   | `/api/transactions/deposit`  | Deposit funds                     | ✅   |
| POST   | `/api/transactions/withdraw` | Withdraw funds                    | ✅   |
| POST   | `/api/transactions/transfer` | Transfer funds to another account | ✅   |
| GET    | `/api/transactions/history`  | View transaction history          | ✅   |

> Endpoint paths are illustrative – update them to match your controllers.

**Example – Transfer**

```http
POST /api/transactions/transfer
Authorization: Bearer <JWT>
Content-Type: application/json

{
  "toAccountNumber": "1234567890",
  "amount": 150.00
}
```

### Common status codes

| Code | Meaning                                     |
| ---- | ------------------------------------------- |
| 200  | Success                                     |
| 400  | Validation error / invalid amount           |
| 401  | Missing, invalid, or expired JWT            |
| 403  | Forbidden                                   |
| 404  | Account or resource not found               |
| 409  | Conflict (e.g. insufficient funds)          |

---

## 🔑 Authentication Flow

1. User registers or logs in from the React client.
2. The API validates credentials and returns a signed **JWT**.
3. The client sends the token in the `Authorization: Bearer` header on every request.
4. A Spring Security filter validates the token and sets the authenticated user for the request.
5. Invalid or expired tokens return `401 Unauthorized`.

---

## 📧 Email Notifications

Emails are sent over SMTP using Spring's mail starter for:

- ✅ Successful deposits, withdrawals, and transfers
- ❌ Failed or rejected transactions
- 🔒 Security events (e.g. new login, password change)

For local development, use [Mailtrap](https://mailtrap.io/) or [MailHog](https://github.com/mailhog/MailHog) to test emails safely.

---

## ☁️ AWS S3 File Storage

Files are stored in an S3 bucket via the AWS SDK for Java v2.

- Keep the bucket **private**.
- Use an IAM user/role with **least privilege**, for example:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": ["s3:PutObject", "s3:GetObject", "s3:DeleteObject"],
      "Resource": "arn:aws:s3:::your-bucket-name/*"
    }
  ]
}
```

---

## 🧪 Testing

```bash
./mvnw test
```

---

## 🛡 Security Notes

- Passwords should be stored hashed (BCrypt) – never in plain text.
- Load JWT, database, SMTP, and AWS secrets from environment variables.
- Wrap balance-changing operations in `@Transactional` to keep data consistent.
- Restrict CORS to your frontend's origin and use HTTPS in production.

---

## 👥 Authors

- **Rishi Rathore** – [@RishiRathore04](https://github.com/RishiRathore04)
- **Vaidansh** – [@thisisvaidansh](https://github.com/thisisvaidansh)
