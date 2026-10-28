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
class GetPeopleWebServiceIT extends SpockTest {
    @LocalServerPort
    private int port

    def person

    def setup() {
        deleteAll()
        and:
        webClient = WebClient.create("http://localhost:" + port)
        headers = new HttpHeaders()
        headers.setContentType(MediaType.APPLICATION_JSON)
        and: "a person"
        person = createPerson(PERSON_1_NAME, PERSON_1_EMAIL)
        and: "another person"
        createPerson(PERSON_2_NAME, PERSON_2_EMAIL)
    }

    def "get people"() {
        when:
        def response = webClient.get()
                .uri('/people')
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToFlux(PersonDto.class)
                .collectList()
                .block()

        then: "check response"
        response.size() == 2
        response.get(0).name == PERSON_1_NAME
        response.get(0).email == PERSON_1_EMAIL
        response.get(1).name == PERSON_2_NAME
        response.get(1).email == PERSON_2_EMAIL

        cleanup:
        deleteAll()
    }

    def "get person"() {
        when:
        def response = webClient.get()
                .uri('/people/' + person.id)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then: "check response"
        response.id == person.id
        response.name == PERSON_1_NAME
        response.email == PERSON_1_EMAIL

        cleanup:
        deleteAll()
    }

    def "get person that does not exist"() {
        when:
        webClient.get()
                .uri('/people/' + 222)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(PersonDto.class)
                .block()

        then:
        def error = thrown(WebClientResponseException)
        error.statusCode == HttpStatus.BAD_REQUEST

        cleanup:
        deleteAll()
    }
}
