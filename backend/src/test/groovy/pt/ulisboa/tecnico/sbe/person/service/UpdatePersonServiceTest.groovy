package pt.ulisboa.tecnico.sbe.person.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException
import spock.lang.Unroll

@DataJpaTest
class UpdatePersonServiceTest extends SpockTest {
    public static final String EXIST = 'exist'
    public static final String NO_EXIST = 'noExist'
    def person

    def setup() {
        given: "a person"
        person = createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
    }

    def 'update person'() {
        given:
        def personDto = createPersonDto(PERSON_2_NAME, PERSON_2_EMAIL)

        when:
        def result = personService.updatePerson(person.id, personDto)

        then:
        result.id == person.id
        result.name == PERSON_2_NAME
        result.email == PERSON_2_EMAIL
        and:
        def storedPerson = personRepository.findById(person.id).get()
        storedPerson.name == PERSON_2_NAME
        storedPerson.email == PERSON_2_EMAIL
    }

    def 'update person keeping the same email'() {
        given:
        def personDto = createPersonDto(PERSON_2_NAME, PERSON_1_EMAIL)

        when:
        def result = personService.updatePerson(person.id, personDto)

        then:
        result.name == PERSON_2_NAME
        result.email == PERSON_1_EMAIL
    }

    def 'update person with the email of another person'() {
        given:
        createPerson(PERSON_2_NAME, PERSON_2_EMAIL)
        def personDto = createPersonDto(PERSON_1_NAME, PERSON_2_EMAIL)

        when:
        personService.updatePerson(person.id, personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == ErrorMessage.PERSON_EMAIL_ALREADY_EXISTS
        and:
        def storedPerson = personRepository.findById(person.id).get()
        storedPerson.email == PERSON_1_EMAIL
    }

    @Unroll
    def 'invalid arguments: personId=#personId'() {
        given:
        def personDto = createPersonDto(PERSON_2_NAME, PERSON_2_EMAIL)

        when:
        personService.updatePerson(getPersonId(personId), personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == errorMessage

        where:
        personId || errorMessage
        null     || ErrorMessage.PERSON_NOT_FOUND
        NO_EXIST || ErrorMessage.PERSON_NOT_FOUND
    }

    def getPersonId(personId) {
        if (personId == EXIST)
            return person.id
        else if (personId == NO_EXIST)
            return 222
        else
            return null
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
