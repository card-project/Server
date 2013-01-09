package models.exceptions.moveExceptions;


public class PlayerIsSlowedException extends IllegalMoveException {

	public PlayerIsSlowedException( String string ) {
		super( string );
	}

	public PlayerIsSlowedException() {
		super ( "Your speed is limited." );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3024942193140528860L;

}
