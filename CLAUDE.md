# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Academic Spring Boot + Hibernate ORM project for managing tour operations (users, routes, stops, purchases, suppliers, services, reviews). Built for BD2 (Bases de Datos 2) at UNLP.

## Commands

```bash
# Start MySQL (required before running tests or app)
docker-compose up -d

# Run all tests
mvn test

# Run a specific test class
mvn test -Dtest=ToursApplicationTests
mvn test -Dtest=ToursQuerysTests

# Build
mvn clean install

# Run app
mvn spring-boot:run
```

## Architecture

**Layered structure:** `config/` → `services/` → `model/` → DB

- **`config/HibernateConfiguration.java`** — defines `SessionFactory`, `DataSource` (MySQL at `localhost:3306/bd2_tours_0`), and transaction manager. DDL is set to `create` (schema is dropped and recreated on every startup).
- **`config/AppConfig.java`** — Spring beans for service and repository implementations.
- **`config/DBInitializerConfig.java`** — triggers `DBInitializer` on startup to seed sample data.
- **`services/ToursService.java`** — interface defining all business logic (CRUD + HQL queries).
- **`model/`** — Hibernate entities. `User` is a base class with `DriverUser` and `TourGuideUser` as subclasses.
- **`utils/DBInitializer.java`** — seeds the DB with suppliers, services, stops, users, routes, purchases, and reviews.
- **`utils/ToursException.java`** — custom checked exception for business rule violations.

## Key conventions

- ORM uses Hibernate directly (not Spring Data JPA). Sessions are obtained via `SessionFactory`.
- Tests in `ToursApplicationTests` are integration tests that hit a real MySQL database — do not mock the DB.
- The database schema is fully regenerated on each application start (`hbm2ddl.auto=create`), so seeded data from `DBInitializer` is always fresh.
- Entities are in `unlp.info.bd2.model` — Hibernate scans this package automatically via `packagesToScan`.
