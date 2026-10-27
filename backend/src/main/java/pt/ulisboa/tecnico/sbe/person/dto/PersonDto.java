package pt.ulisboa.tecnico.sbe.person.dto;

import pt.ulisboa.tecnico.sbe.person.domain.Person;

// Data Transfer Object: what is sent to and received from the frontend.
public class PersonDto {
    private Integer id;
    private String name;
    private String email;

    public PersonDto() {
    }

    public PersonDto(Person person) {
        setId(person.getId());
        setName(person.getName());
        setEmail(person.getEmail());
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
}
