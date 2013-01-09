package models.exceptions.moveExceptions;

import models.cards.Card;

public class PlayerIsAttackedException extends IllegalMoveException {

	public PlayerIsAttackedException( String string ) {
		super( string );
	}
	
	public PlayerIsAttackedException( Card attackedCard ) {
		super( "You are under attack : " + attackedCard );
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 604480215604759146L;

}
