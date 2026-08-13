# Pilot Coupon Dispatch Service

Spring Boot backend for the Pilot Coupon Dispatch Service project, with JWT-based authentication, MySQL persistence (JPA/Hibernate), file/static serving, and Actuator health/info endpoints.

## Tech Stack

- Java 25
- Spring Boot 4.0.5
- Maven Wrapper (`./mvnw`)
- MySQL

## Project Structure

```
src/main/java/com/pilotcoupondispatchservice/
├── config/         # Security, CORS, Swagger/OpenAPI, and other bean configuration
├── security/        # JWT filter, entry point, access-denied handler
├── modules/          # Feature modules (each with controller/service/repository/dto/entity/mapper)
│   ├── auth/
│   ├── users/
│   ├── roles/
│   ├── pilots/
│   ├── bookings/
│   ├── coupons/
│   ├── routes/
│   ├── vehicles/
│   ├── dashboard/
│   └── base/         # Shared base controller/service classes
├── exceptions/       # Global exception handling
├── validations/      # Custom validation annotations/logic
├── schedulers/       # Scheduled jobs
├── interceptors/      # Request interceptors
├── jackson/          # Jackson (de)serialization customizations
├── payloads/         # Shared request/response payload types
├── dao/, dto/, enums/, constants/, utils/, annotations/
```

Each `modules/*` package generally follows: `controller` → `service` → `repository`, with `dto`/`mapper`/`entity` alongside. Admin- and owner-scoped operations are split into separate controllers (e.g. `AdminBookingController`, `OwnerBookingController`) and secured via role-based authorization (`@EnableMethodSecurity` in `SecurityConfig`).

## Prerequisites

- Java 25
- MySQL database reachable from the app

## Environment Configuration

The app loads environment values from a root `.env` file (`spring.config.import=optional:file:.env[.properties]`).

1. Copy and edit the example file:

```bash
cp .env_example .env
```

2. Fill required values.

### Environment Variables

Only a `dev` profile is currently defined (`application.yml` + `application-dev.yml`); it's active by default.

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | No | `dev` | Spring profile |
| `SERVER_PORT` | No | `8080` | HTTP server port |
| `DB_URL` | No | `jdbc:mysql://localhost:3306/pilot_service?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC` | MySQL JDBC URL |
| `DB_USERNAME` | No | `root` | Database username |
| `DB_PASSWORD` | No | `root` | Database password |
| `DB_MAX_POOL_SIZE` | No | `10` | Hikari max pool size |
| `DB_MIN_IDLE` | No | `2` | Hikari min idle |
| `JWT_SECRET` | Yes | none | JWT signing secret (recommended: `openssl rand -base64 64`) |
| `JWT_ACCESS_EXPIRATION` | No | `PT1H` | Access token expiration (ISO-8601 duration) |
| `JWT_REFRESH_EXPIRATION` | No | `P7D` | Refresh token expiration (ISO-8601 duration) |
| `CORS_ALLOWED_ORIGINS` | No | `http://localhost:3000,http://localhost:5173` | Comma-separated allowed CORS origins |
| `MAIL_HOST` | No | `smtp.gmail.com` | SMTP host |
| `MAIL_PORT` | No | `587` | SMTP port |
| `MAIL_USERNAME` | Yes | none | SMTP auth username |
| `MAIL_PASSWORD` | Yes | none | SMTP auth password |
| `FILE_IMAGE_UPLOAD_PATH` | No | `static/uploads` | Local storage path for uploaded images (`file.image-upload-path`) |
| `ADMIN_NAME` | No | `Admin User` | Seeded default admin user's name |
| `ADMIN_EMAIL` | No | `admin@example.com` | Seeded default admin user's email (used for login) |
| `ADMIN_PHONE` | No | `1234567890` | Seeded default admin user's phone |
| `ADMIN_PASSWORD` | No | `Admin@123456` | Seeded default admin user's password |

On first startup, `InitialSeeder` creates the `ADMIN` role and a default admin user (from the `ADMIN_*` variables above) if none exists yet — use these credentials to obtain your first JWT via the admin login endpoint below.

## Run Locally

```bash
./mvnw spring-boot:run
```

Default URLs:

- Public ping: `GET http://localhost:8080/api/1.0.0/public/ping`
- Secured ping: `GET http://localhost:8080/api/1.0.0/secured/ping`
- Admin login: `POST http://localhost:8080/api/1.0.0/public/auth/admin/login`
- Owner login: `POST http://localhost:8080/api/1.0.0/public/auth/owner/login`
- Owner registration: `POST http://localhost:8080/api/1.0.0/public/auth/owner/registraton`
- Owner OTP verify + signup: `POST http://localhost:8080/api/1.0.0/public/auth/owner/verify-otp-and-signup`
- Forgot password (request OTP): `POST http://localhost:8080/api/1.0.0/public/auth/forgot-password`
- Reset password (verify OTP + set new password): `POST http://localhost:8080/api/1.0.0/public/auth/reset-password`
- Refresh token: `POST http://localhost:8080/api/1.0.0/public/auth/refresh`
- Health: `GET http://localhost:8080/actuator/health`
- Info: `GET http://localhost:8080/actuator/info`
- Swagger UI: `GET http://localhost:8080/swagger-ui.html`
- OpenAPI spec: `GET http://localhost:8080/v3/api-docs`

Swagger UI is publicly accessible, but secured endpoints still require a token: log in via the auth endpoint to get a JWT, then click **Authorize** in Swagger UI and paste the token (`bearer-jwt` scheme) to call protected APIs from the docs page.

## Testing

```bash
./mvnw test
```

Uses `spring-boot-starter-test` (JUnit 5, Mockito, Spring Test). Test sources mirror the `main` package layout under `src/test/java/com/pilotcoupondispatchservice/`.

## Logs and Static Files

- Runtime logs are written to `./logs/current.log` and rotated under `./logs/archive/...`.
- Static/uploaded files are served from `./static` and available under `${url.base}/static/**`.
