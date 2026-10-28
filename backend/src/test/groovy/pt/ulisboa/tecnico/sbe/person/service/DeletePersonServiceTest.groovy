package pt.ulisboa.tecnico.sbe.person.service

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.TestConfiguration
import pt.ulisboa.tecnico.sbe.BeanConfiguration
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage
import pt.ulisboa.tecnico.sbe.exceptions.SBEException

@DataJpaTest
class DeletePersonServiceTest extends SpockTest {
    def person

    def setup() {
        given: "a person"
        person = createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
    }

    def 'delete person'() {
        when:
        personService.deletePerson(person.id)

        then:
        personRepository.findAll().size() == 0
    }

    def 'delete a person that does not exist'() {
        when:
        personService.deletePerson(222)

        then:
        def error = thrown(SBEException)
        error.getErrorMessage() == ErrorMessage.PERSON_NOT_FOUND
        and:
        personRepository.findAll().size() == 1
    }

    @TestConfiguration
    static class LocalBeanConfiguration extends BeanConfiguration {}
}
