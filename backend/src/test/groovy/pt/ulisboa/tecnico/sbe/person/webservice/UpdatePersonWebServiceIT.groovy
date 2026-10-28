package pt.ulisboa.tecnico.sbe.person.webservice

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import pt.ulisboa.tecnico.sbe.SpockTest
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UpdatePersonWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def person
    def personDto

    def setup() {
        deleteAll()
        and:
        webClient = WebClient.create("http://localhost:" + port)
        headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        and: "a person"
        person = createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
        and: "the new person info"
        personDto = createPersonDto(PERSON_2_NAME, PERSON_2_EMAIL)
    }

    def 'update person'() {
        when:
        def response = webClient.put()
                .uri('/people/' + person.id)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .bodyValue(personDto)
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        response.id == person.id
        response.name == PERSON_2_NAME
        response.email == PERSON_2_EMAIL
        and:
        def storedPerson = personRepository.findById(person.id).get()
        storedPerson.name == PERSON_2_NAME
        storedPerson.email == PERSON_2_EMAIL

        cleanup:
        deleteAll()
    }

    def 'update person with error'() {
        given:
        personDto.email = null

        when:
        webClient.put()
                .uri('/people/' + person.id)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .bodyValue(personDto)
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        def error = thrown(WebClientResponseException)
        error.statusCode == HttpStatus.BAD_REQUEST
        and:
        def storedPerson = personRepository.findById(person.id).get()
        storedPerson.name == PERSON_1_NAME
        storedPerson.email == PERSON_1_EMAIL

        cleanup:
        deleteAll()
    }
}
