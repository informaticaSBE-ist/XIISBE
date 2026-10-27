package pt.ulisboa.tecnico.sbe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pt.ulisboa.tecnico.sbe.person.domain.Person;
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto;
import pt.ulisboa.tecnico.sbe.person.repository.PersonRepository;

/**
 * Seeds a couple of people on startup (only if their email is missing),
 * so the frontend has something to show.
 *
 * DEV CONVENIENCE ONLY
 */
@Component
public class DataInitializer implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final PersonRepository personRepository;

    public DataInitializer(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public void run(String... args) {
        seed("André Sá", "andre.sa@tecnico.ulisboa.pt");
        seed("Luís Moleiro", "luis.moleiro@tecnico.ulisboa.pt");
    }

    private void seed(String name, String email) {
        if (personRepository.existsByEmail(email)) {
            return;
        }

        PersonDto personDto = new PersonDto();
        personDto.setName(name);
        personDto.setEmail(email);
        personRepository.save(new Person(personDto));
        logger.info("Seeded demo person: {}", email);
    }
}
