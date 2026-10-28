package pt.ulisboa.tecnico.sbe

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.ulisboa.tecnico.sbe.person.PersonService

// @DataJpaTest only creates the repositories, so the services used by the tests are declared here
@TestConfiguration
class BeanConfiguration {

    @Bean
    PersonService personService() {
        return new PersonService()
    }
}
