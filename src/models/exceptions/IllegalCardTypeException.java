package models.exceptions;

public class IllegalCardTypeException extends Exception {

	// -------------- CONSTANTS -------------- //
	
	private static final long serialVersionUID = 8759890175295664330L;

	// -------------- CONSTRUCTORS -------------- //
	
	public IllegalCardTypeException( String string ) {
		super( string );
	}

	public IllegalCardTypeException() {
	}

}
