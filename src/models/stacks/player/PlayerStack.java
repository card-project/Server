package models.stacks.player;

import java.io.Serializable;
import java.util.LinkedList;

import models.cards.Card;
import models.cards.CardType;
import models.exceptions.IllegalCardTypeException;
import models.players.Player;
import models.stacks.CardsStack;

/**
 * Structure containing {@link Card} objects belonging to a {@link Player}.
 * This {@link Player} knows the whole content of the {@link PlayerStack}.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public abstract class PlayerStack extends CardsStack implements Serializable {


	// ------------ ATTRIBUTES ------------ //

	protected LinkedList<Card> cards;
	private static final long serialVersionUID = 451651676405961396L;	

	// ------------ CONSTRUCTORS ------------ //

	public PlayerStack() {
		this.cards = new LinkedList<Card>();
	}

	// ------------ METHODS ------------ //

	public java.util.Iterator<Card> iterator() {
		return cards.iterator();
	}
	
	/**
	 * Check whether a {@link Card} having the {@link CardType} exists 
	 * in the current {@link PlayerStack}.
	 * @param ct {@link CardType} to search.
	 * @return Whether a {@link Card} having this {@link CardType} exists.
	 */
	public boolean exists( CardType ct ) {
		for ( Card c : this.cards ) {
			if ( c.getType() == ct ) {
				return true;
			}
		}
		return false;
	}
	
	public Card get( int index ) {
		return this.cards.get( index );
	}

	public void remove( Card item ) {
		this.cards.remove( item );
	}
	
	/**
	 * Shift a {@link Card} from the current {@link PlayerStack} to
	 * the destination {@link CardsStack}.
	 * @param destination {@link CardsStack} to shift to.
	 * @param c {@link Card} to shift.
	 * @throws IllegalCardTypeException Whether the move is not authorized.
	 */
	public void shiftTo( CardsStack destination, Card c ) throws IllegalCardTypeException {
		destination.push( c );
		this.cards.remove( c );
	}
	
	@Override
	public void push( Card item ) throws IllegalCardTypeException {
		this.cards.addFirst( item );
	}

	@Override
	public Card pop() {
		return this.cards.pop();
	}

	@Override
	public Card peek() {
		return this.cards.peek();
	}
	
	@Override
	public boolean isEmpty() {
		return this.cards.isEmpty();
	}
	
	@Override
	public int size() {
		return this.cards.size();
	}

	@Override
	public String toString() {
		return this.cards.toString();
	}

	public void removeAll() {
		this.cards.clear();
	}

	// ------------ GETTERS ------------ //

}
