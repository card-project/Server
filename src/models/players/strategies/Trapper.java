package models.players.strategies;

import java.util.ArrayList;

import models.cards.Card;
import models.cards.CardFamily;
import models.players.AIPlayer;
import models.players.Player;

/**
 * {@link AIPlayer} strategy.
 * 
 * Keep his safety for a tricky move (coup fourré).
 * 
 * @author Adrien SAUNIER
 * @author Simon RENOULT
 * @version 0.1
 */
public class Trapper extends Protector {

	// ------------ CONSTRUCTORS ------------ //
	
	public Trapper( AIPlayer player, ArrayList<Player> opponents ) {
		super( player, opponents );
	}
	
	// ------------ METHODS ------------ //

	@Override
	public Card chooseCardToPlay() {
		Card chosenCard = null;
		
		if ( owner.isAttacked() ) {
			CardFamily attackingFamily = owner.getBattleStack().peek().getFamily();
			chosenCard = owner.getHandStack().getRemedyOf( attackingFamily );
		} else if ( owner.isSlowed() ) {
			chosenCard = owner.getHandStack().getRemedyOf( CardFamily.Speed );
		} 
		
		return chosenCard;
	}
}
