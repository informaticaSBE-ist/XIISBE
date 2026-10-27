package pt.ulisboa.tecnico.sbe.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

// Body of the error responses. The frontend shows `message` to the user.
public class SBEExceptionDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime timestamp;

    private String message;

    private String debugMessage;

    public SBEExceptionDto(SBEException e) {
        this.timestamp = LocalDateTime.now();
        this.message = e.getMessage();
    }

    public SBEExceptionDto(Throwable e) {
        this.timestamp = LocalDateTime.now();
        this.message = "Unexpected error";
        this.debugMessage = e.getLocalizedMessage();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }
}
