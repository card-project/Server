package models.players.strategies;

import java.util.ArrayList;

import models.cards.Card;
import models.cards.CardFamily;
import models.cards.RemedyCard;
import models.cards.SafetyCard;
import models.players.AIPlayer;
import models.players.Player;
import models.stacks.game.DiscardStack;
import models.stacks.game.GameStack;

/**
 * AI player strategy.
 * 
 * Draw a safety/remedy as soon as possible.
 * Play a safety/remedy as soon as possible.
 * Discard duplicate remedy and hazard.
 * 
 * @author Adrien SAUNIER
 * @author Simon RENOULT
 * @version 0.1
 */
public class Protector extends Behavior {

	// -------------- ATTRIBUTES -------------- //
	
	private ArrayList<Player> opponents;
	
	// ------------ CONSTRUCTORS ------------ //

	public Protector( AIPlayer owner, ArrayList<Player> opponents ) {
		super( owner );
		this.opponents = opponents;
	}
	
	// ------------ METHODS ------------ //

	/**
	 * Priorities :
	 * 1 - GoRoll if not started
	 * 2 - Correspondent remedy/safety if attacked 
	 * 3 - New remedy 
	 * 
	 * @return The chosen {@link GameStack} to draw on.
	 */
	@Override
	public GameStack chooseStackToDraw() {
		GameStack chosenStack = null;

		if ( ! DiscardStack.getInstance().isEmpty() ) {
			Card discardedCard = DiscardStack.getInstance().peek();
			
			if ( discardedCard != null ) {
				if ( ! owner.hasStarted() && discardedCard.isGoRoll() ) {
					chosenStack = DiscardStack.getInstance();
				} else if ( owner.isAttacked() ) {
					if ( discardedCard.counteract( owner.getBattleStack().peek().getFamily() ) ) {
						chosenStack = DiscardStack.getInstance();
					}
				} else if ( owner.isSlowed() ) {
					if ( discardedCard.counteract( CardFamily.Speed ) ) {
						chosenStack = DiscardStack.getInstance();
					}
				} else if ( owner.hasStarted() && discardedCard instanceof RemedyCard ) {
					chosenStack = DiscardStack.getInstance();
				}
			}
		}
		
		return chosenStack;
	}

	/**
	 * Priorities :
	 * 1 : player is attacked, play the correspondent remedy/safety
	 * 2 : player is not attacked, play a safety
	 *
	 * @return The chosen {@link Card} to play.
	 */
	@Override
	public Card chooseCardToPlay() {
		Card chosenCard = null;
		
		if ( owner.isAttacked() ) {
			CardFamily attackingFamily = owner.getBattleStack().peek().getFamily();
			if ( ( chosenCard = owner.getHandStack().getSafetyOf( attackingFamily ) ) == null ) {
				chosenCard = owner.getHandStack().getRemedyOf( attackingFamily );
			}
		} else if ( owner.isSlowed() ) {
			if ( ( chosenCard = owner.getHandStack().getSafetyOf( CardFamily.Speed ) ) == null ) {
				chosenCard = owner.getHandStack().getRemedyOf( CardFamily.Speed );
			}
		} 
		
		if ( chosenCard == null ) {
			for ( Card c : owner.getHandStack() ) {
				if ( c instanceof SafetyCard ) {
					chosenCard = c;
				}
			}
		}
		
		return chosenCard;
	}

	/**
	 * Priorities :
	 * 1 : Remedy having the same family than a played safety.
	 * 2 : Duplicate remedy.
	 * 3 : 1st remedy in player's hand (~Random).
	 * Finally check whether the discarded helps an opponent.
	 * 
	 * @return The chosen {@link Card} to discard.
	 */
	@Override
	public Card chooseCardToDiscard() {
		Card cardToDiscard = null;
		
		if ( cardToDiscard == null ) {
			for ( Card safety : owner.getSafetyStack() ) {
				for ( CardFamily safetyFamily : safety.getFamilies() ) {
					if ( cardToDiscard == null ) {
						cardToDiscard = owner.getHandStack().getRemedyOf( safetyFamily );
					}
				}
			}
		}
		
		if ( cardToDiscard == null ) {
			Card testingCard = null;
			for( Card handCard : owner.getHandStack() ) {
				if ( handCard instanceof RemedyCard ) {
					if ( testingCard == null ) {
						testingCard = handCard;
					} else {
						if ( testingCard.getFamily() == handCard.getFamily() ) {
							cardToDiscard = testingCard;
						}
					}
				}
			}
		}
		
		if ( cardToDiscard == null ) {
			for ( Card handCard : owner.getHandStack() ) {
				if ( handCard instanceof RemedyCard && cardToDiscard == null ) {
					cardToDiscard = handCard;
				}
			}
		}
		
//		if ( cardToDiscard != null ) {
//			for ( Player opp : opponents ) {
//				
//				System.out.println("Battle stack : " + opp.getBattleStack());
//				System.out.println("Remedy family : " + opp.getBattleStack().getRemedyFamily());
//				System.out.println("Card family : " + cardToDiscard.getFamily());
//				
//				if ( cardToDiscard.getFamily() == opp.getBattleStack().getRemedyFamily() ) {
//					cardToDiscard = null;
//				}
//			}
//		}
		
		return cardToDiscard;
	}

}
