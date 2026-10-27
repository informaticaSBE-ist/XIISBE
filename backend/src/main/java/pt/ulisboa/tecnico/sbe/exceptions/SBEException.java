package pt.ulisboa.tecnico.sbe.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SBEException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(SBEException.class);

    private final ErrorMessage errorMessage;

    public SBEException(ErrorMessage errorMessage) {
        super(errorMessage.label);
        logger.error(errorMessage.label);
        this.errorMessage = errorMessage;
    }

    public SBEException(ErrorMessage errorMessage, String value) {
        super(String.format(errorMessage.label, value));
        logger.error(String.format(errorMessage.label, value));
        this.errorMessage = errorMessage;
    }

    public SBEException(ErrorMessage errorMessage, int value) {
        super(String.format(errorMessage.label, value));
        logger.error(String.format(errorMessage.label, value));
        this.errorMessage = errorMessage;
    }

    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }
}
