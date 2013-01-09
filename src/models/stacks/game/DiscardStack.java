package models.stacks.game;

import models.cards.Card;

/**
 * Structure containing discarded {@link Card}.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public class DiscardStack extends GameStack {
	
	private static class DiscardStackHolder {
		private static final DiscardStack INSTANCE = new DiscardStack();
	}

	public static DiscardStack getInstance() {
		return DiscardStackHolder.INSTANCE;
	}

	private DiscardStack() {
	}
}
