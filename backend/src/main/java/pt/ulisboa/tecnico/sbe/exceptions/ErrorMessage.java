package pt.ulisboa.tecnico.sbe.exceptions;

public enum ErrorMessage {
    // Person
    PERSON_NOT_FOUND("Não existe nenhuma pessoa com o id %d"),
    PERSON_NAME_NOT_VALID("O nome da pessoa não é válido"),
    PERSON_EMAIL_NOT_VALID("O email %s não é válido"),
    PERSON_EMAIL_ALREADY_EXISTS("Já existe uma pessoa com o email %s");

    public final String label;

    ErrorMessage(String label) {
        this.label = label;
    }
}
