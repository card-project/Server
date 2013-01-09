package models.players.strategies;

import models.cards.Card;
import models.players.AIPlayer;
import models.players.Brain;
import models.stacks.game.GameStack;
import models.stacks.player.HandStack;

/**
 * Interface setting the allowed {@link Brain} operations.
 * 
 * @author Adrien SAUNIER
 * @author Simon RENOULT
 * @version 1.0
 */
public interface Strategy {
	
	/**
	 * Determine which stack the {@link AIPlayer} draws on.
	 * The choice depends on the implemented algorithm.
	 * 
	 * @return {@link GameStack} to draw on.
	 */
	GameStack chooseStackToDraw();	
	
	/**
	 * Determine which card to pick in {@link AIPlayer}'s {@link HandStack}.
	 * The choice depends on the implemented algorithm.
	 * 
	 * @return {@link Card} to play.
	 */
	Card chooseCardToPlay();
	
	/**
	 * Determine which card to discard in {@link AIPlayer}'s {@link HandStack}.
	 * The choice depends on the implemented algorithm.
	 * 
	 * @return {@link Card} to discard.
	 */
	Card chooseCardToDiscard();
}
