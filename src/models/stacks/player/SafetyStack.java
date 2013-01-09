package models.stacks.player;

import models.cards.Card;
import models.cards.SafetyCard;
import models.exceptions.IllegalCardTypeException;

/**
 * Structure containing exclusively {@link SafetyCard}.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public class SafetyStack extends PlayerStack {

	// ------------ METHODS ------------ //

	@Override
	public void push( Card item ) throws IllegalCardTypeException {
		if ( ! ( item instanceof SafetyCard ) ) {
			throw new IllegalCardTypeException();
		} else {
			this.cards.push( item );
		}
	}
}
