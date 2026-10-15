# Multi-Tenant GST Invoice & Tax Compliance Platform

A multi-tenant billing and tax-compliance backend for businesses operating across multiple locations. Built with Java 21
and Spring Boot, the platform focuses on GST invoicing, transactional invoice numbering, branch-level access control,
and tenant data isolation.

> **Status: In active development.** Core management and authorization modules are implemented. Invoice workflows,
> reliability improvements, and external compliance integrations are under development. See the [Roadmap](#roadmap) for
> the current implementation status.

**Live demo:** Coming soon | **Walkthrough video:** Coming soon

---

## What It Does

- **Multi-tenant businesses:** Each tenant manages its own users, customers, products, GST registrations, and branches.
- **GST registrations:** Businesses can manage GST registrations across states, with GSTIN format and PAN consistency
  validation.
- **Branch management:** Physical branches are associated with GST registrations.
- **Access control:** Users have roles defining what they can do and branch-level access grants defining where they can
  do it.
- **GST invoicing:** Invoice workflows are being developed to calculate CGST, SGST, or IGST based on applicable
  transaction rules.
- **Transactional invoice numbering:** Implementing a per-branch invoice counter designed to maintain sequential
  numbering within each financial year under concurrent requests.
- **Historical invoice accuracy:** Invoice-item snapshots are part of the invoice design to preserve historical prices
  and GST rates when product information changes.

## Engineering Highlights

| Engineering challenge           | Approach                                                                                                                                                                                    |
|---------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Transactional invoice numbering | Use a per-branch counter row locked with PostgreSQL `SELECT ... FOR UPDATE` within the invoice transaction. The counter update and invoice insert must participate in the same transaction. |
| Tenant isolation                | Resolve tenant identity from the authenticated JWT rather than trusting client-supplied tenant identifiers. Scope tenant-owned data access to the authenticated tenant.                     |
| Branch-level authorization      | Maintain branch access grants separately from user roles, allowing permissions to be scoped by both capability and location.                                                                |
| Historical invoice correctness  | Snapshot unit prices and GST rates onto invoice items at creation time so later product changes do not alter historical invoice values.                                                     |
| Database integrity              | Use database constraints, foreign keys, and appropriate delete restrictions to enforce data integrity beyond application-level validation.                                                  |
| GSTIN validation                | Validate GSTIN format and enforce tenant-level PAN consistency to help prevent registrations belonging to different legal entities from being grouped under one tenant.                     |

*These are the intended designs and implementation approaches. Invoice-related behavior and concurrency guarantees
remain subject to completion and testing.*

## Tech Stack

### Backend

- Java 21
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- Hibernate
- JWT authentication
- Maven

### Database and Migrations

- PostgreSQL
- Flyway

### Mapping and Boilerplate Reduction

- MapStruct
- Lombok

### Planned Testing and Reliability Tools

- Testcontainers for PostgreSQL integration testing
- Resilience4j for fault tolerance during external GSTN integration

## Architecture

The application follows a **feature-based modular monolith** architecture. Each business domain is organized into its
own feature module, keeping related components together while separating application responsibilities.

```text
multitenantgstplatform/
├── common/
│   ├── response/
│   ├── exception/
│   └── factory/
├── feature/
│   ├── tenant/
│   ├── gstregistration/
│   ├── branch/
│   ├── master/
│   ├── product/
│   ├── customer/
│   ├── invoice/
│   ├── authorization/
│   └── authentication/
└── infrastructure/
    ├── config/
    ├── security/
    └── seeder/
```

### Core Domain Relationships

```text
Tenant
  ├── GST Registrations
  │     └── Branches
  │           └── Invoices
  │                 └── Invoice Items
  │
  ├── Users
  │     ├── Roles and Permissions
  │     └── Branch Access Grants
  │
  ├── Customers
  └── Products

Products
  ├── GST Rates
  ├── HSN Codes
  └── Units
```

Customers and products are tenant-scoped and shared across branches. Invoices are associated with the relevant branch
and GST registration.

### Backend Layers

- **Controllers:** Handle HTTP requests and responses.
- **Services:** Implement business logic and application workflows through service interfaces and implementations.
- **Repositories:** Manage database access using Spring Data JPA.
- **Entities:** Represent persistent domain models and database relationships.
- **DTOs:** Define structured request and response payloads, separating API contracts from persistence models.
- **Mappers:** Convert between DTOs and entities using MapStruct.
- **Security:** Handles JWT authentication, request authorization, and security configuration.
- **Exception Handling:** Centralizes application errors through custom exceptions and a global exception handler.
- **Response Handling:** Provides a standardized API response structure through reusable response components.
- **Seeders:** Initialize required application data, including states, users, roles, modules, and permissions.
- **Database Migrations:** Flyway manages version-controlled database schema changes.

### Request Processing Flow

```text
Client Application
        |
        v
 REST Controller
        |
        v
     Service
        |
        +------> Mapper
        |           |
        v           v
    Repository <--- Entity
        |
        v
    PostgreSQL
```

The mapper is used wherever data conversion is required. Authentication and authorization are handled through Spring
Security and the application's security components.

### Shared Components

The `common` package contains reusable application components:

- **ApiResponse:** Standardized API response structure.
- **ApiResponseFactory:** Reusable API response creation.
- **GlobalExceptionHandler:** Centralized exception handling.
- **Custom Exceptions:** Represent application-specific error conditions.

The `infrastructure` package contains application-wide configuration, security utilities, and database seeders.

## Roadmap

### Completed

- [x] Tenant management
- [x] JWT authentication
- [x] Role-Based Access Control (RBAC) with multi-role users
- [x] GST registrations and branches
- [x] GST registration PAN consistency validation
- [x] Branch access grant management
- [x] Master data: GST rates, HSN codes, units, and states
- [x] Customer management
- [x] Product management

### In Progress / Planned

- [ ] Invoice creation and GST calculation
- [ ] Transactional, row-locked invoice numbering
- [ ] Enforce branch-access checks on all relevant invoice endpoints
- [ ] Complete idempotent invoice creation, including concurrent-request handling
- [ ] Add PostgreSQL integration tests for concurrent invoice creation
- [ ] Verify numbering under 50 parallel requests, checking for duplicate or skipped numbers
- [ ] Implement the transactional outbox pattern for reliable event publishing
- [ ] Integrate with the GSTN e-invoice sandbox
- [ ] Add Resilience4j timeout, retry, and circuit-breaker policies
- [ ] Implement audit logging
- [ ] Generate invoice PDFs
- [ ] Build GST summary and sales reports
- [ ] Evaluate PostgreSQL Row-Level Security as an additional tenant-isolation layer
- [ ] Run load tests and document measured results

## Running Locally

### Prerequisites

- Java 21
- PostgreSQL
- Maven (the Maven wrapper is included)

### 1. Clone the Repository

```bash
git clone https://github.com/navaneeth06/multi-tenant-gst-platform.git
cd multi-tenant-gst-platform
```

### 2. Create the Database

Create a PostgreSQL database:

```sql
CREATE
DATABASE gst_platform;
```

### 3. Configure Environment Variables

Configure the following environment variables using your preferred local development setup. If you use a `.env` file, do
not commit it to the repository.

```env
JWT_SECRET=replace_with_a_long_random_secret_of_at_least_32_characters
DB_URL=jdbc:postgresql://localhost:5432/gst_platform
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password
```

Ensure your Spring Boot configuration loads these variables correctly. Spring Boot does not automatically load an
arbitrary `.env` file by default.

### 4. Build the Application

```bash
./mvnw clean package -DskipTests
```

### 5. Run the Application

```bash
java -jar target/multi-tenant-gst-platform-0.0.1-SNAPSHOT.jar
```

Flyway applies pending database migrations on startup if migration execution is enabled in the application
configuration.

### 6. Seed Initial Data

If the application's command-line seeding option is configured, run:

```bash
java -jar target/multi-tenant-gst-platform-0.0.1-SNAPSHOT.jar --seed=authorization
```

Confirm which records this command initializes before relying on it for a fresh installation.

### 7. Authenticate

Send a login request to:

```http
POST /api/v1/auth/login
```

Use the returned JWT in subsequent requests:

```http
Authorization: Bearer <token>
```

Refer to the application's API configuration or Swagger/OpenAPI documentation for the exact request payload and
available endpoints.

## Scope and Limitations

- GSTN integration targets the government sandbox; the project does not file actual GST returns.
- The initial scope focuses on standard forward-charge GST.
- Cess, reverse charge, and composition schemes are outside the current scope.
- Multiple branches under a GST registration may maintain separate invoice series, subject to applicable
  invoice-numbering rules and the project's requirements.
- Invoice concurrency guarantees, idempotency, and branch-access enforcement require implementation and verification
  before being considered complete.
- PDF generation, reporting, audit logging, and additional reliability features remain on the roadmap.

## Project Goals

This project explores practical backend engineering concepts, including:

- Java and Spring Boot application design
- Modular monolith architecture
- Multi-tenant SaaS systems
- JWT authentication and authorization
- Branch-level access control
- Transaction management and pessimistic locking
- Concurrent invoice-number allocation
- Database constraints and relational integrity
- Historical financial data preservation
- GST calculation workflows
- Database migration management
- Integration and concurrency testing
- Centralized exception handling
- Standardized API responses

