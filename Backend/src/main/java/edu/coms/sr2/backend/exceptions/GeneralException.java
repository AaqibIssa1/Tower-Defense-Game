package edu.coms.sr2.backend.exceptions;

/**
 * Exception class to be used for inheritance purposes, primarily intended for 
 * type-based exception handling
 * @author Nathan
 *
 */

public class GeneralException extends RuntimeException
{
	private static final long serialVersionUID = -1513700578719340305L;
	
	public GeneralException() {}
	public GeneralException(String message) {
		super(message);
	}
}
