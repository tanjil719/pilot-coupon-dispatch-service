# Pilot Coupon Dispatch Service

Spring Boot backend for the Pilot Coupon Dispatch Service project, with JWT-based authentication, MySQL persistence (JPA/Hibernate), file/static serving, and Actuator health/info endpoints.

## Tech Stack

- Java 25
- Spring Boot 4.0.5
- Maven Wrapper (`./mvnw`)
- MySQL
- Docker / Docker Compose

## Prerequisites

- Java 25 (for local run)
- Docker + Docker Compose (for containerized run)
- MySQL database reachable from the app

## Environment Configuration

The app loads environment values from a root `.env` file (`spring.config.import=optional:file:.env[.properties]`).

1. Copy and edit the example file:

```bash
cp .env_example .env
```

2. Fill required values.

### Environment Variables

| Variable | Required | Default | Description |
| --- | --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | No | `dev` | Spring profile (`dev`, `staging`, `prod`) |
| `SERVER_PORT` | No | `8080` | HTTP server port |
| `DB_URL` | Yes (`staging`/`prod`) | `jdbc:mysql://localhost:3306/pilot_service?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&useLegacyDatetimeCode=false&useUnicode=yes&characterEncoding=UTF-8&serverTimezone=UTC` (dev) | MySQL JDBC URL |
| `DB_USERNAME` | Yes (`staging`/`prod`) | `root` (dev) | Database username |
| `DB_PASSWORD` | Yes (`staging`/`prod`) | `root` (dev) | Database password |
| `DB_MAX_POOL_SIZE` | No | profile-based (`10` dev, `20` staging, `30` prod) | Hikari max pool size |
| `DB_MIN_IDLE` | No | profile-based (`2` dev, `5` staging, `10` prod) | Hikari min idle |
| `JWT_SECRET` | Yes | none | JWT signing secret (recommended: `openssl rand -base64 64`) |
| `JWT_ACCESS_EXPIRATION` | No | `PT1H` | Access token expiration (ISO-8601 duration) |
| `JWT_REFRESH_EXPIRATION` | No | `P7D` | Refresh token expiration (ISO-8601 duration) |

### Optional App Property

| Property | Default | Description |
| --- | --- | --- |
| `file.image-upload-path` | `static/uploads` | Local storage path for uploaded images |

## Run Locally

```bash
./mvnw spring-boot:run
```

Default URLs:

- Public ping: `GET http://localhost:8080/api/1.0.0/public/ping`
- Secured ping: `GET http://localhost:8080/api/1.0.0/secured/ping`
- Refresh token: `POST http://localhost:8080/api/1.0.0/public/auth/refresh`
- Health: `GET http://localhost:8080/actuator/health`
- Info: `GET http://localhost:8080/actuator/info`

## Run with Docker Compose

Build and start:

```bash
docker compose up --build -d
```

Stop:

```bash
docker compose down
```

Notes:

- `docker-compose.yml` uses `network_mode: host`.
- The backend container reads `.env` from the project root (`/app/.env`).
- Database/service addresses should be resolvable from the host network context.
- Volumes are mounted for:
  - `./logs -> /app/logs`
  - `./static -> /app/static`

## Logs and Static Files

- Runtime logs are written to `./logs/current.log` and rotated under `./logs/archive/...`.
- Static/uploaded files are served from `./static` and available under `${url.base}/static/**`.
