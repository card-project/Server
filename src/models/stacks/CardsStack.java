package models.stacks;

import java.io.Serializable;

import models.cards.Card;

public abstract class CardsStack implements Handable, Iterable<Card>, Serializable {
	private static final long serialVersionUID = 1606706409925249093L;
}
