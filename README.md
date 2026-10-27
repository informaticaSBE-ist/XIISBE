# XII SBE - Semana da Bioengenharia

## Dependencies

- Require download
  - [Java 21](https://adoptium.net/)
  - [Maven](https://maven.apache.org/download.cgi)
  - [Node 20+](https://nodejs.org/en/)
  - [Docker](https://www.docker.com/)
- No download required
  - [Spring-boot](https://spring.io/)
  - [Vue.js](https://vuejs.org/) + [Vuetify](https://vuetifyjs.com/)

## Run Locally

### Database

Start the database with Docker (from the project root, where `docker-compose.yml` is):

```bash
docker compose up -d
```

To stop it:

```bash
docker compose down
```

### Backend

Create your local config from the example (only once):

```bash
cp ./backend/src/main/resources/application.properties.example ./backend/src/main/resources/application.properties
```

If you're running the database with Docker, the values already match `docker-compose.yml`.

Build and run:

```bash
cd ./backend
mvn clean spring-boot:run
```

The backend runs on http://localhost:8080 (try http://localhost:8080/people).

### Frontend

Create your local config from the example (only once):

```bash
cp ./frontend/example.env ./frontend/.env
```

Install the dependencies and run:

```bash
cd ./frontend
npm i
npm run dev
```

Open the address printed in the terminal (usually http://localhost:5173).

## Access the Database

```bash
docker compose exec postgres psql -U postgres sbedb
```

## Project structure

```
backend/src/main/java/pt/ulisboa/tecnico/sbe/
├── SBEApplication.java
├── config/            CORS + demo data
├── exceptions/        SBEException + ErrorMessage (all error messages live here)
└── person/            one package per entity
    ├── PersonController.java   web service: HTTP endpoints only
    ├── PersonService.java      use cases, transactions, rules that need the database
    ├── domain/Person.java      the entity and its own rules (verifyInvariants)
    ├── dto/PersonDto.java      what is sent to / received from the frontend
    └── repository/PersonRepository.java   database access

frontend/src/
├── models/            TypeScript versions of the DTOs
├── services/RemoteService.ts   every call to the backend
├── views/             one folder per page
├── components/        shared pieces (top bar, error message, loading)
└── router/            page routes
```

### Adding a new entity (e.g. `Activity`)

Copy the `person` package and follow the same layers:

1. `activity/domain/Activity.java`: the `@Entity`, built from an `ActivityDto`, checking its own rules in `verifyInvariants()`.
2. `activity/dto/ActivityDto.java`: plain fields + getters/setters, with a constructor that takes an `Activity`.
3. `activity/repository/ActivityRepository.java`: `extends JpaRepository<Activity, Integer>`.
4. `activity/ActivityService.java`: `@Transactional(isolation = Isolation.READ_COMMITTED)` methods that return DTOs.
5. `activity/ActivityController.java`: `@GetMapping`/`@PostMapping`/... that only call the service.
6. New error messages go in `exceptions/ErrorMessage.java`, thrown as `new SBEException(...)`.
7. Frontend: `models/ActivityDto.ts`, the calls in `RemoteService.ts`, a view in `views/activities/` and a route in `router/index.ts`.
