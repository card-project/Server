package models.stacks.player;

import models.cards.Card;
import models.cards.CardFamily;
import models.cards.HazardCard;
import models.exceptions.IllegalCardTypeException;
import models.players.Player;
import models.stacks.game.DiscardStack;

/**
 * Structure containing {@link Card} objects and belonging to a 
 * {@link Player}.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public class BattleStack extends PlayerStack {

	// ------------ METHODS ------------ //

	private boolean initialGoRollIsPlayed = false;

	// ------------ METHODS ------------ //

	@Override
	public void push( Card item ) throws IllegalCardTypeException {
		if ( item instanceof HazardCard ) {
			this.cards.push( item );
		} else if ( ( !initialGoRollIsPlayed && item.isGoRoll() ) ) {
			initialGoRollIsPlayed = true;
			this.cards.push( item );
		} else {
			throw new IllegalCardTypeException();
		}
	}

	/**
	 * Discard every {@link HazardCard} existing in the current {@link BattleStack}.
	 */
	public void discardHazards() {
		for ( int i = 0; i < super.cards.size(); i++ ) {
			if ( super.cards.get( i ) instanceof HazardCard ) {
				try {
					this.shiftTo( DiscardStack.getInstance(), super.cards.get( i ) );
				} catch ( IllegalCardTypeException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public CardFamily getRemedyFamily() {
		CardFamily cureFamily = null;
		for ( Card c : this ) {
			if ( c instanceof HazardCard ) {
				cureFamily = c.getFamily();
			}
		}
		
		return cureFamily;
	}

	// ------------ GETTERS ------------ //

	public boolean initialGoRollIsPlayed() {
		return initialGoRollIsPlayed;
	}
}
