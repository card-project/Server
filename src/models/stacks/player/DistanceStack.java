package models.stacks.player;

import models.cards.Card;
import models.cards.DistanceCard;
import models.cards.HazardCard;
import models.exceptions.IllegalCardTypeException;
import models.exceptions.moveExceptions.MaxNumberOfDistance200IsReachedException;
import models.players.Player;
import models.stacks.game.DiscardStack;

/**
 * Structure containing {@link Card} objects and belonging
 * to a {@link Player}.
 * 
 * @author Simon RENOULT
 * @version 1.0.1
 */
public class DistanceStack extends PlayerStack {

	// ------------ CONSTANTS ------------ //
	
	public static final int MAX_DISTANCE200 = 2;
	public static final int BONUS_100 = 100;
	public static final int BONUS_300 = 300;
	
	// ------------ ATTRIBUTES ------------ //

	private int BONUS_100_CPT = 0;
	private int BONUS_300_CPT = 0;
	
	// ------------ METHODS ------------ //

	@Override
	public void push( Card item ) throws IllegalCardTypeException {
		if ( item instanceof DistanceCard ) {
			if ( ( ( DistanceCard ) item ).getRange() == 200 && maxNumberOfDistance200IsReached() ) {
				throw new MaxNumberOfDistance200IsReachedException();
			}
		}

		super.cards.push( item );
	}
	
	/**
	 * Get the amount of driven kilometers.
	 * @return The amount of driven kilometers.
	 */
	public int getTraveledDistance() {
		int currentDistance = 0;
		for( Card c : super.cards ) {
			if ( c instanceof DistanceCard ) {
				currentDistance += ( (DistanceCard ) c ).getRange();
			}
		}
		
		return currentDistance + BONUS_100 * BONUS_100_CPT + BONUS_300 * BONUS_300_CPT;
	}
	
	/**
	 * Check whether the max number of Distance 200 is reached.
	 * @return Whether the maximum distance 200 is reached.
	 */
	public Boolean maxNumberOfDistance200IsReached() {
		int distances200Counter = 0;
		for ( Card c : super.cards ) {
			if ( c instanceof DistanceCard ) {
				if ( ( ( DistanceCard ) c ).getRange() == 200 ) {
					distances200Counter++;
				}
			}
		}
		
		return distances200Counter == MAX_DISTANCE200;
	}

	/**
	 * Discard every {@link HazardCard} existing in the current {@link DistanceStack}.
	 */
	public void discardHazards() {
		for ( int i = 0; i < super.cards.size() ; i++ ) {
			if ( super.cards.get( i ) instanceof HazardCard ) {
				try {
					shiftTo( DiscardStack.getInstance(), super.cards.get( i ) );
				} catch ( IllegalCardTypeException e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void increaseBy100() {
		BONUS_100_CPT++;
	}

	public void increaseBy300() {
		BONUS_300_CPT++;
	}
	
	public int getBonus100() {
		return this.BONUS_100_CPT;
	}
	
	public int getBonus300() {
		return this.BONUS_300_CPT;
	}
	
	public void resetBonuses() {
		BONUS_100_CPT = 0;
		BONUS_300_CPT = 0;
	}
	
}
