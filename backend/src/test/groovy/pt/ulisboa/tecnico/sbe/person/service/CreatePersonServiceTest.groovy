package pt.ulisboa.tecnico.sbe.person.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException
import spock.lang.Unroll

@DataJpaTest
class CreatePersonServiceTest extends SpockTest {

    def 'create person'() {
        given:
        def personDto = createPersonDto(PERSON_1_NAME, PERSON_1_EMAIL)

        when:
        def result = personService.createPerson(personDto)

        then:
        result.id != null
        result.name == PERSON_1_NAME
        result.email == PERSON_1_EMAIL
        and:
        personRepository.findAll().size() == 1
        def storedPerson = personRepository.findAll().get(0)
        storedPerson.id == result.id
        storedPerson.name == PERSON_1_NAME
        storedPerson.email == PERSON_1_EMAIL
    }

    def 'create person with an email that already exists'() {
        given:
        createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
        def personDto = createPersonDto(PERSON_2_NAME, PERSON_1_EMAIL)

        when:
        personService.createPerson(personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == ErrorMessage.PERSON_EMAIL_ALREADY_EXISTS
        and:
        personRepository.findAll().size() == 1
    }

    @Unroll
    def 'invalid arguments: name=#name | email=#email'() {
        given:
        def personDto = createPersonDto(name, email)

        when:
        personService.createPerson(personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == errorMessage
        and:
        personRepository.findAll().size() == 0

        where:
        name          | email          || errorMessage
        null          | PERSON_1_EMAIL || ErrorMessage.PERSON_NAME_NOT_VALID
        PERSON_1_NAME | "no-at-sign"   || ErrorMessage.PERSON_EMAIL_NOT_VALID
    }

    def 'create person without dto'() {
        when:
        personService.createPerson(null)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == ErrorMessage.PERSON_NAME_NOT_VALID
        and:
        personRepository.findAll().size() == 0
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
