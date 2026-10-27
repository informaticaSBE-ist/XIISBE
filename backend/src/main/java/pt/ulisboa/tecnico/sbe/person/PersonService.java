package pt.ulisboa.tecnico.sbe.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.sbe.exceptions.SBEException;
import pt.ulisboa.tecnico.sbe.person.domain.Person;
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto;
import pt.ulisboa.tecnico.sbe.person.repository.PersonRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage.*;

// Service: the use cases. Loads/saves domain objects through the repository
// and converts them to DTOs. Rules that need the database (e.g. unique email) go here.
@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<PersonDto> getPeople() {
        return personRepository.findAll().stream()
                .sorted(Comparator.comparing(Person::getId))
                .map(PersonDto::new)
                .toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PersonDto getPerson(Integer personId) {
        return new PersonDto(fetchPersonOrThrow(personId));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PersonDto createPerson(PersonDto personDto) {
        if (personDto == null) throw new SBEException(PERSON_NAME_NOT_VALID);

        if (personRepository.existsByEmail(personDto.getEmail())) {
            throw new SBEException(PERSON_EMAIL_ALREADY_EXISTS, personDto.getEmail());
        }

        Person person = new Person(personDto);
        personRepository.save(person);

        return new PersonDto(person);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public PersonDto updatePerson(Integer personId, PersonDto personDto) {
        if (personDto == null) throw new SBEException(PERSON_NAME_NOT_VALID);

        Person person = fetchPersonOrThrow(personId);

        if (!Objects.equals(personDto.getEmail(), person.getEmail())
                && personRepository.existsByEmail(personDto.getEmail())) {
            throw new SBEException(PERSON_EMAIL_ALREADY_EXISTS, personDto.getEmail());
        }

        person.update(personDto);

        return new PersonDto(person);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deletePerson(Integer personId) {
        Person person = fetchPersonOrThrow(personId);

        personRepository.delete(person);
    }

    private Person fetchPersonOrThrow(Integer personId) {
        if (personId == null) throw new SBEException(PERSON_NOT_FOUND);

        return personRepository.findById(personId)
                .orElseThrow(() -> new SBEException(PERSON_NOT_FOUND, personId));
    }
}
