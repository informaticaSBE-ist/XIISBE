package pt.ulisboa.tecnico.sbe.person.webservice

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import pt.ulisboa.tecnico.sbe.SpockTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeletePersonWebServiceIT extends SpockTest {
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
    }

    def 'delete person'() {
        when:
        webClient.delete()
                .uri('/people/' + person.id)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(Void.class)
                .block()

        then:
        personRepository.count() == 0

        cleanup:
        deleteAll()
    }

    def 'delete person that does not exist'() {
        when:
        webClient.delete()
                .uri('/people/' + 222)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(Void.class)
                .block()

        then:
        def error = thrown(WebClientResponseException)
        error.statusCode == HttpStatus.BAD_REQUEST
        personRepository.count() == 1

        cleanup:
        deleteAll()
    }
}
