package ro.pub.cs.pdsd.buddystalker.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested user does not exist")
public class UserNotFoundException extends RuntimeException {

}
