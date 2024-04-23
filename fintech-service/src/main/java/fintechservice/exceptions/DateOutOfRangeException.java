package fintechservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
public class DateOutOfRangeException extends RuntimeException{

	private static final long serialVersionUID = 1068765979896800015L;

}
