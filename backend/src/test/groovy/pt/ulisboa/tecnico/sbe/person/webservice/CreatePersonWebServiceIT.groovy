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
class CreatePersonWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def personDto

    def setup() {
        deleteAll()
        and:
        webClient = WebClient.create("http://localhost:" + port)
        headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        and:
        personDto = createPersonDto(PERSON_1_NAME, PERSON_1_EMAIL)
    }

    def 'create person'() {
        when:
        def response = webClient.post()
                .uri('/people')
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .bodyValue(personDto)
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        response.id != null
        response.name == PERSON_1_NAME
        response.email == PERSON_1_EMAIL
        and:
        personRepository.findAll().size() == 1
        def storedPerson = personRepository.findAll().get(0)
        storedPerson.name == PERSON_1_NAME
        storedPerson.email == PERSON_1_EMAIL

        cleanup:
        deleteAll()
    }

    def 'create person with error'() {
        given:
        personDto.name = null

        when:
        webClient.post()
                .uri('/people')
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .bodyValue(personDto)
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        def error = thrown(WebClientResponseException)
        error.statusCode == HttpStatus.BAD_REQUEST
        personRepository.count() == 0

        cleanup:
        deleteAll()
    }

    def 'create person with an email that already exists'() {
        given:
        createPerson(PERSON_2_NAME, PERSON_1_EMAIL)

        when:
        webClient.post()
                .uri('/people')
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .bodyValue(personDto)
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        def error = thrown(WebClientResponseException)
        error.statusCode == HttpStatus.BAD_REQUEST
        personRepository.count() == 1

        cleanup:
        deleteAll()
    }
}
