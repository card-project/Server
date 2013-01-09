package models.exceptions.moveExceptions;

public class PlayerIsProtectedException extends IllegalMoveException {

	public PlayerIsProtectedException( String string ) {
		super( string );
	}

	public PlayerIsProtectedException() {
		this("Your opponent is protected from this kind of attack.");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9173639518374167546L;

}
