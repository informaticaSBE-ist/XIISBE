// Commands that talk directly to the database (see the queryDatabase task in cypress.config.ts)
const credentials = {
  user: Cypress.env('psql_db_username'),
  host: Cypress.env('psql_db_host'),
  database: Cypress.env('psql_db_name'),
  password: Cypress.env('psql_db_password'),
  port: Cypress.env('psql_db_port')
}

const PEOPLE_COLUMNS = 'people (name, email)'

Cypress.Commands.add('deleteAll', () => {
  cy.task('queryDatabase', {
    query: 'DELETE FROM PEOPLE',
    credentials: credentials
  })
})

Cypress.Commands.add('createDemoEntities', () => {
  cy.task('queryDatabase', {
    query: 'INSERT INTO ' + PEOPLE_COLUMNS + generatePersonTuple('Demo Person 1', 'demo1@mail.com'),
    credentials: credentials
  })
  cy.task('queryDatabase', {
    query: 'INSERT INTO ' + PEOPLE_COLUMNS + generatePersonTuple('Demo Person 2', 'demo2@mail.com'),
    credentials: credentials
  })
})

function generatePersonTuple(name, email) {
  return " VALUES ('" + name + "', '" + email + "')"
}
