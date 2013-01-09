package models.exceptions;

public class AliasAlreadyChosenException extends Exception {

	private static final long serialVersionUID = -6853857556141054492L;

	public AliasAlreadyChosenException( String string ) {
		super( string );
	}
}
