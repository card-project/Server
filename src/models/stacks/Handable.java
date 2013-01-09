package models.stacks;

import models.cards.Card;
import models.exceptions.IllegalCardTypeException;

/**
 * Allow a stack to be handled through this interface methods.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public interface Handable {

	public boolean isEmpty();

	public Card pop();
	
	public void push( Card item ) throws IllegalCardTypeException;
	
	public Card peek();
	
	public int size();

	public String toString();
}
