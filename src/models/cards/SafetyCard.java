package models.cards;

import java.io.Serializable;

import models.exceptions.IllegalCardTypeException;
import models.players.Player;
import models.stacks.player.DistanceStack;

/**
 * @author Simon RENOULT
 * @version 1.0
 */
public class SafetyCard extends Card implements Serializable{

	// ------------ ATTRIBUTES ------------ //
	
	private static final long serialVersionUID = -6457539955870758101L;	
	
	// ------------ CONSTRUCTORS ------------ //

	protected SafetyCard( CardFamily initialFamily, CardType cardType ) {
		super( initialFamily, cardType );
	}
	
	// ------------ METHODS ------------ //
	
	public boolean isPlayableOn( Player p ) {
		return true;
	}
	
	/**
	 * Play the current {@link SafetyCard} on the chosen {@link Player} and take it
	 * off from his hand. Determine also whether this move implies the player to replay.
	 * 
	 * @param p {@link Player} who plays the {@link SafetyCard}.
	 * @return Whether the {@link Player} replay after this {@link Card} has been played. 
	 */
	public boolean playOn( Player p, int distanceGoal ) {
		
		// Move the safety to the right stack and remove it from the player's hand
		try {
			p.getHandStack().shiftTo( p.getSafetyStack(), this );
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}
		
		// As the card is a safety, increase the traveled distance by 100
		if ( p.getTraveledDistance() + DistanceStack.BONUS_100 < distanceGoal ) {
			p.getDistanceStack().increaseBy100();
		}
		
		// Remove the hazard if the safety's family corresponds
		if ( p.isAttacked() ) {
			for( CardFamily cf : this.getFamilies() ) {
				if ( cf == p.getBattleStack().peek().getFamily() ) {
					p.getBattleStack().discardHazards();
				}
			}
		}
		
		if ( p.isSlowed() && this.isRightOfWay() ) {
			p.getDistanceStack().discardHazards();
		}
		
		return true;
	}

	public boolean playCoupFourre( Player coupFourreInitiator,  Player assailant, HazardCard hazardCard, int distanceGoal ) {
		assailant.discard( hazardCard );
		
		try {
			coupFourreInitiator.getHandStack().shiftTo( coupFourreInitiator.getSafetyStack(), this );
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}

		// As the card is a safety, increase the traveled distance by 100
		if ( coupFourreInitiator.getTraveledDistance() + DistanceStack.BONUS_100 < distanceGoal ) {
			coupFourreInitiator.getDistanceStack().increaseBy300();
		}
		
		return true;
	}
}
