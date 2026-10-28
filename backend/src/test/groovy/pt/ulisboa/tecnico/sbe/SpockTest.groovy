package pt.ulisboa.tecnico.sbe

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.function.client.WebClient
import pt.ulisboa.tecnico.sbe.person.PersonService
import pt.ulisboa.tecnico.sbe.person.domain.Person
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto
import pt.ulisboa.tecnico.sbe.person.repository.PersonRepository
import spock.lang.Specification

class SpockTest extends Specification {
    // remote requests

    WebClient webClient
    HttpHeaders headers

    // person

    public static final String PERSON_1_NAME = "Person 1 Name"
    public static final String PERSON_2_NAME = "Person 2 Name"
    public static final String PERSON_1_EMAIL = "person1@mail.com"
    public static final String PERSON_2_EMAIL = "person2@mail.com"

    @Autowired
    PersonService personService

    @Autowired
    PersonRepository personRepository

    def createPersonDto(name, email) {
        def personDto = new PersonDto()
        personDto.setName(name)
        personDto.setEmail(email)
        return personDto
    }

    def createPerson(name, email) {
        def person = new Person(createPersonDto(name, email))
        personRepository.save(person)
        return person
    }

    // clean database

    def deleteAll() {
        personRepository.deleteAll()
    }
}
