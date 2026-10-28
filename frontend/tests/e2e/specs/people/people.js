describe('People', () => {
  beforeEach(() => {
    cy.deleteAll()
    cy.createDemoEntities()
  })

  afterEach(() => {
    cy.deleteAll()
  })

  it('list people', () => {
    cy.visit('/people')

    cy.get('[data-cy="peopleTable"] tbody tr')
      .should('have.length', 2)
      .eq(0)
      .children()
      .should('contain', 'Demo Person 1')
      .should('contain', 'demo1@mail.com')
  })

  it('create person', () => {
    const NAME = 'Maria Silva'
    const EMAIL = 'maria.silva@mail.com'

    cy.intercept('POST', '/people').as('create')
    cy.intercept('GET', '/people').as('getPeople')

    cy.visit('/people')
    cy.wait('@getPeople')

    cy.get('[data-cy="createButton"]').click()
    cy.get('[data-cy="nameInput"] input').type(NAME)
    cy.get('[data-cy="emailInput"] input').type(EMAIL)
    cy.get('[data-cy="saveButton"]').click()
    cy.wait('@create')

    // the table is reloaded with the new person
    cy.wait('@getPeople')
    cy.get('[data-cy="peopleTable"] tbody tr').should('have.length', 3)
    cy.contains('[data-cy="peopleTable"] tbody tr', EMAIL).should('contain', NAME)
  })

  it('create person with an email that already exists', () => {
    cy.intercept('POST', '/people').as('create')

    cy.visit('/people')

    cy.get('[data-cy="createButton"]').click()
    cy.get('[data-cy="nameInput"] input').type('Other Person')
    cy.get('[data-cy="emailInput"] input').type('demo1@mail.com')
    cy.get('[data-cy="saveButton"]').click()
    cy.wait('@create').its('response.statusCode').should('eq', 400)

    cy.get('[data-cy="errorMessage"]').should('contain', 'demo1@mail.com')
  })

  it('delete person', () => {
    cy.intercept('DELETE', '/people/*').as('delete')

    cy.visit('/people')

    cy.contains('[data-cy="peopleTable"] tbody tr', 'demo1@mail.com')
      .find('[data-cy="deleteButton"]')
      .click()
    cy.wait('@delete')

    cy.get('[data-cy="peopleTable"] tbody tr').should('have.length', 1)
    cy.get('[data-cy="peopleTable"]').should('not.contain', 'demo1@mail.com')
  })
})
