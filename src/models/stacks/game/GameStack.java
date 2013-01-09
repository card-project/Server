package models.stacks.game;

import java.util.Iterator;
import java.util.Stack;

import models.Game;
import models.cards.Card;
import models.cards.CardType;
import models.exceptions.IllegalCardTypeException;
import models.stacks.CardsStack;

/**
 * Stack belonging to a {@link Game} object.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public abstract class GameStack extends CardsStack {

	// ------------ ATTRIBUTES ------------ //
	
	protected Stack<Card> cards;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public GameStack() {
		this.cards = new Stack<Card>();
	}
	
	// ------------ METHODS ------------ //

	@Override
	public Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	/**
	 * Determine whether the selected {@link CardType} exists. 
	 * @param ct {@link CardType} searched.
	 * @return Whether the {@link CardType} exists in the current {@link GameStack}.
	 */
	public boolean exists( CardType ct ) {
		for ( Card c : this.cards ) {
			if ( c.getType() == ct ) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Shift a {@link Card} from the current {@link GameStack} to
	 * the destination {@link CardsStack}.
	 * @param destination {@link CardsStack} to shift to.
	 * @param c {@link Card} to shift.
	 * @throws IllegalCardTypeException Whether the move is not authorized.
	 */
	public void shiftTopCardTo( CardsStack destination ) {
		try {
			destination.push( this.cards.pop() );
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	@Override
	public Card pop() {
		return cards.pop();
	}
	
	@Override
	public void push( Card item ) {
		this.cards.push( item );
	}
	
	@Override
	public Card peek() {
		return ( this.isEmpty() ) ? null : this.cards.peek();
	}
	
	@Override
	public int size() {
		return this.cards.size();
	}
	
	@Override
	public String toString() {
		return this.cards.toString();
	}
}
