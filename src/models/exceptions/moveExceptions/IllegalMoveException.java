package models.exceptions.moveExceptions;

public class IllegalMoveException extends Exception {

	private static final long serialVersionUID = 2220602030025582655L;

	public IllegalMoveException() {
	}
	
	public IllegalMoveException( String string ) {
		super( string );
	}
}
