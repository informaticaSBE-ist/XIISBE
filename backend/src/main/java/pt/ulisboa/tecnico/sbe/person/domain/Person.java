package pt.ulisboa.tecnico.sbe.person.domain;

import jakarta.persistence.*;
import pt.ulisboa.tecnico.sbe.exceptions.SBEException;
import pt.ulisboa.tecnico.sbe.person.dto.PersonDto;

import static pt.ulisboa.tecnico.sbe.exceptions.ErrorMessage.*;

// Domain class: holds the data and the rules (invariants) of a person.
// Rules that only depend on the person itself are checked here, in verifyInvariants().
@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    public Person() {
    }

    public Person(PersonDto personDto) {
        setName(personDto.getName());
        setEmail(personDto.getEmail());

        verifyInvariants();
    }

    public void update(PersonDto personDto) {
        setName(personDto.getName());
        setEmail(personDto.getEmail());

        verifyInvariants();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private void verifyInvariants() {
        nameIsRequired();
        emailIsRequired();
    }

    private void nameIsRequired() {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new SBEException(PERSON_NAME_NOT_VALID);
        }
    }

    private void emailIsRequired() {
        if (this.email == null || !this.email.contains("@")) {
            throw new SBEException(PERSON_EMAIL_NOT_VALID, this.email);
        }
    }
}
