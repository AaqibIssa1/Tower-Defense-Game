package edu.coms.sr2.backend.http.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import edu.coms.sr2.backend.exceptions.GeneralException;

/**
 * An exception handler for Spring to use on un-handled exceptions.
 * Provides handling for exceptions that inherit from our GeneralException class
 * @author Nathan
 *
 */
@ControllerAdvice
public class GeneralHandler 
{
	/**
	 * Handler method for instances of GeneralException. Currently just responds with an error string
	 * @param e The exception to handle	
	 * @return The response entity, containing a simple error string
	 */
	@ExceptionHandler(value = GeneralException.class)
	public ResponseEntity<String> handle(GeneralException e)
	{
		return new ResponseEntity<String>("ERROR: " + e.getMessage(), HttpStatus.BAD_REQUEST);
	}
}
