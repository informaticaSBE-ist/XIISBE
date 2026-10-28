package pt.ulisboa.tecnico.sbe.person.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException

@DataJpaTest
class GetPeopleServiceTest extends SpockTest {
    def person

    def setup() {
        given: "a person"
        person = createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
        and: "another person"
        createPerson(PERSON_2_NAME, PERSON_2_EMAIL)
    }

    def 'get two people'() {
        when:
        def result = personService.getPeople()

        then:
        result.size() == 2
        result.get(0).name == PERSON_1_NAME
        result.get(1).name == PERSON_2_NAME
    }

    def 'get one person'() {
        when:
        def result = personService.getPerson(person.id)

        then:
        result.id == person.id
        result.name == PERSON_1_NAME
        result.email == PERSON_1_EMAIL
    }

    def 'get a person that does not exist'() {
        when:
        personService.getPerson(222)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == ErrorMessage.PERSON_NOT_FOUND
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
