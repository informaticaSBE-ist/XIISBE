package pt.ulisboa.tecnico.sbe.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto;

import java.util.List;

// Web service: the HTTP endpoints. Only translates requests into service calls.
@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping("/people")
    public List<PersonDto> getPeople() {
        return personService.getPeople();
    }

    @GetMapping("/people/{personId}")
    public PersonDto getPerson(@PathVariable Integer personId) {
        return personService.getPerson(personId);
    }

    @PostMapping("/people")
    public PersonDto createPerson(@RequestBody PersonDto personDto) {
        return personService.createPerson(personDto);
    }

    @PutMapping("/people/{personId}")
    public PersonDto updatePerson(@PathVariable Integer personId, @RequestBody PersonDto personDto) {
        return personService.updatePerson(personId, personDto);
    }

    @DeleteMapping("/people/{personId}")
    public void deletePerson(@PathVariable Integer personId) {
        personService.deletePerson(personId);
    }
}
