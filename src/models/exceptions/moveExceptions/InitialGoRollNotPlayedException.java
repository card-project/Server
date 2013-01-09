package models.exceptions.moveExceptions;

public class InitialGoRollNotPlayedException extends IllegalMoveException {

	public InitialGoRollNotPlayedException() {
		super( "Initial GoRoll card has not been played yet." );
	}
	
	public InitialGoRollNotPlayedException( String string ) {
		super( string );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 8881390631613377253L;

}
