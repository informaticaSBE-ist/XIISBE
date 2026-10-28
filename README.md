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

### Backend tests

The tests are written with [Spock](https://spockframework.org/) (Groovy).

Unit tests (`*Test`): domain and service, run on an in-memory H2 database, so no Docker needed:

```bash
cd ./backend
mvn clean -Ptest test
```

Integration tests (`*IT`): webservice, start the whole backend and call it over HTTP. They use the
database from `application.properties`, so start it first (`docker compose up -d`). They **delete all the data** in it:

```bash
cd ./backend
mvn clean -Ptest-int verify
```

A single class or method:

```bash
mvn clean -Ptest test -Dtest=CreatePersonServiceTest
mvn clean -Ptest test -Dtest='CreatePersonServiceTest#create person'
mvn clean -Ptest-int verify -Dit.test=CreatePersonWebServiceIT
```

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

### Frontend tests

End-to-end tests with [Cypress](https://www.cypress.io/): they open the app in a browser,
click through it and check what shows up. The specs are in `frontend/tests/e2e/specs/`.

Create your local config from the example (only once). The values already match `docker-compose.yml`:

```bash
cp ./frontend/cypress.env.json.example ./frontend/cypress.env.json
```

The database, backend and frontend must all be running (see above). The tests **delete all the people** in the database.

```bash
cd ./frontend
npm run cypress      # opens the Cypress window, pick a spec and watch it run
npm run test:e2e     # runs every spec in the terminal
```

If `npm i` skipped the Cypress download, run `npx cypress install`.
If Cypress fails with `bad option: --smoke-test` (it happens in the VS Code terminal), run it from a
normal terminal, or clear the variable first: `$env:ELECTRON_RUN_AS_NODE=""` (PowerShell) / `unset ELECTRON_RUN_AS_NODE` (bash).

Elements are found by their `data-cy` attribute (e.g. `<v-btn data-cy="createButton">` → `cy.get('[data-cy="createButton"]')`).

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

backend/src/test/groovy/pt/ulisboa/tecnico/sbe/
├── SpockTest.groovy           shared constants, helpers (createPerson) and deleteAll()
├── BeanConfiguration.groovy   services needed by the @DataJpaTest tests
└── person/
    ├── domain/                *MethodTest: the entity rules
    ├── service/               *ServiceTest: the use cases
    └── webservice/            *WebServiceIT: the HTTP endpoints

frontend/src/
├── models/            TypeScript versions of the DTOs
├── services/RemoteService.ts   every call to the backend
├── views/             one folder per page
├── components/        shared pieces (top bar, error message, loading)
└── router/            page routes

frontend/tests/e2e/
├── specs/people/people.js   one spec per page
└── support/database.js      cy.deleteAll() and cy.createDemoEntities(), straight SQL on the database
```
