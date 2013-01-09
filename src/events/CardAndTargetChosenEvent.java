package events;

import java.util.EventObject;

import models.players.Player;

public class CardAndTargetChosenEvent extends EventObject{

	// ------------ ATTRIBUTES ------------ //
	
	private static final long serialVersionUID = 8142072036389589731L;
	private Player opponent;

	// ------------ CONSTRUCTORS ------------ //
	
	public CardAndTargetChosenEvent(Player opponent) {
		super(opponent);
		this.opponent = opponent;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	
}
