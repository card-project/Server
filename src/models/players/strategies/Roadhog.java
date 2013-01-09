package models.players.strategies;

import java.util.ArrayList;

import models.cards.Card;
import models.cards.DistanceCard;
import models.cards.HazardCard;
import models.players.AIPlayer;
import models.players.Player;
import models.stacks.game.DiscardStack;
import models.stacks.game.GameStack;

/**
 * {@link AIPlayer} strategy.
 * 
 * Draw a hazard as soon as possible.
 * Play a hazard as soon as possible.
 * Discard a duplicate hazard.
 * 
 * @author Adrien SAUNIER
 * @author Simon RENOULT
 * @version 0.1
 */
public class Roadhog extends Behavior {

	// -------------- CONSTANTS -------------- //
	
	private static final int MAX_HAZARD = 2;
	
	// ------------ ATTRIBUTES ------------ //
	
	private ArrayList<Player> opponents;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public Roadhog(AIPlayer player, ArrayList<Player> opponents) {
		super( player );
		this.opponents = opponents;
	}
	 	
	// ------------ METHODS ------------ //
	
	/**
	 * If no hazard in hand, draw a hazard if possible.
	 */
	@Override
	public GameStack chooseStackToDraw() {
		Card discardedCard = DiscardStack.getInstance().peek();
		GameStack chosenStack = null;
		
		if ( discardedCard != null ) {
			if ( ! maxHazardIsReached() && ! owner.getHandStack().containsHazard() && discardedCard instanceof HazardCard ) {
				for ( Player p : opponents ) {
					if ( ! p.isProtectedFrom( (HazardCard) discardedCard ) ) {
						chosenStack = DiscardStack.getInstance();
					}
				}
			}
		} 
		
		return chosenStack;
	}

	@Override
	public Card chooseCardToPlay() {
		Card cardToPlay = null;
		
		for ( Card handCard : owner.getHandStack() ) {
			if ( handCard instanceof HazardCard && cardToPlay == null ) {
				for ( Player p : opponents ) {
					if ( ( ( HazardCard ) handCard ).isPlayableOn( p ) ) {
						cardToPlay = handCard;
					}
				}
			}
		}
		
		return cardToPlay;
	}

	public Player chooseTargetToAttack() {
		Player target = null;
		
		for ( Card handCard : owner.getHandStack() ) {
			if ( handCard instanceof HazardCard && target == null ) {
				for ( Player p : opponents ) {
					if ( ( ( HazardCard ) handCard ).isPlayableOn( p ) ) {
						target = p;
					}
				}
			}
		}
		
		return target;
	}

	/**
	 * Priorities :
	 * 1 : Protected types
	 * 2 : Duplicate
	 * 3 : 1st Hazard (~Random).
	 */
	@Override
	public Card chooseCardToDiscard() {
		Card cardToDiscard = null;

		if ( opponents.size() == 1 ) {
			for ( Player p : opponents ) {
				for ( Card safety : p.getSafetyStack() ) {
					for ( Card handCard : owner.getHandStack() ) {
						if ( ! ( handCard instanceof DistanceCard ) ) {
							if ( safety.getFamily() == handCard.getFamily() ) {
								cardToDiscard = handCard;
							}
						}
					}
				}
			}	
		}
		
		if ( cardToDiscard == null ) {
			Card tmp = null;
			for ( Card handCard : owner.getHandStack() ) {
				if ( handCard instanceof HazardCard ) {
					if ( tmp == null ) {
						tmp = handCard;
					} else {
						if ( tmp.getFamily() == handCard.getFamily() ) {
							cardToDiscard = tmp;
						}
					}
				}
			}
		}
		
		if ( cardToDiscard == null ) {
			for ( Card handCard : owner.getHandStack() ) {
				if ( handCard instanceof HazardCard && cardToDiscard == null ) {
					cardToDiscard = handCard;
				}
			}
		}

		return cardToDiscard;
	}
	
	private boolean maxHazardIsReached() {
		int hazardCount = 0;
		for ( Card c : owner.getHandStack() ) {
			if ( c instanceof HazardCard ) {
				hazardCount++;
			}
		}
		
		return hazardCount >= MAX_HAZARD;
	}

}
