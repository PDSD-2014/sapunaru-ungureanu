package ro.pub.cs.pdsd.buddystalker.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Username already in use")
public class UsernameValidationException extends RuntimeException {

}
