package pt.ulisboa.tecnico.sbe.person.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException
import spock.lang.Unroll

@DataJpaTest
class UpdatePersonMethodTest extends SpockTest {
    def person

    def setup() {
        given: "a person"
        person = new Person(createPersonDto(PERSON_1_NAME, PERSON_1_EMAIL))
    }

    def "update person"() {
        given:
        def personDto = createPersonDto(PERSON_2_NAME, PERSON_2_EMAIL)

        when:
        person.update(personDto)

        then: "checks results"
        person.getName() == PERSON_2_NAME
        person.getEmail() == PERSON_2_EMAIL
    }

    @Unroll
    def "update person and violate invariants: name=#name | email=#email"() {
        given:
        def personDto = createPersonDto(name, email)

        when:
        person.update(personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == errorMessage

        where:
        name          | email          || errorMessage
        null          | PERSON_2_EMAIL || ErrorMessage.PERSON_NAME_NOT_VALID
        "   "         | PERSON_2_EMAIL || ErrorMessage.PERSON_NAME_NOT_VALID
        PERSON_2_NAME | null           || ErrorMessage.PERSON_EMAIL_NOT_VALID
        PERSON_2_NAME | "no-at-sign"   || ErrorMessage.PERSON_EMAIL_NOT_VALID
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
