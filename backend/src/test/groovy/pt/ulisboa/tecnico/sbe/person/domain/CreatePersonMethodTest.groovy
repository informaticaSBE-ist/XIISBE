package pt.ulisboa.tecnico.sbe.person.domain

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException
import spock.lang.Unroll

@DataJpaTest
class CreatePersonMethodTest extends SpockTest {
    def personDto

    def setup() {
        given: "person info"
        personDto = createPersonDto(PERSON_1_NAME, PERSON_1_EMAIL)
    }

    def "create person"() {
        when:
        def result = new Person(personDto)

        then: "checks results"
        result.getName() == PERSON_1_NAME
        result.getEmail() == PERSON_1_EMAIL
    }

    @Unroll
    def "create person and violate invariants: name=#name | email=#email"() {
        given:
        personDto.setName(name)
        personDto.setEmail(email)

        when:
        new Person(personDto)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == errorMessage

        where:
        name          | email          || errorMessage
        null          | PERSON_1_EMAIL || ErrorMessage.PERSON_NAME_NOT_VALID
        "   "         | PERSON_1_EMAIL || ErrorMessage.PERSON_NAME_NOT_VALID
        PERSON_1_NAME | null           || ErrorMessage.PERSON_EMAIL_NOT_VALID
        PERSON_1_NAME | "no-at-sign"   || ErrorMessage.PERSON_EMAIL_NOT_VALID
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
