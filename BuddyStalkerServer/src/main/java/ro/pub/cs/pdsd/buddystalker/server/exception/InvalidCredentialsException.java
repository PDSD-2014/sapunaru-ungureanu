package ro.pub.cs.pdsd.buddystalker.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid username and secret combination")
public class InvalidCredentialsException extends RuntimeException {

}
