package events;

import java.util.EventObject;

import models.players.Player;

public class PlayerHasPlayedEvend extends EventObject {

	private static final long serialVersionUID = 7607135562891382647L;

	public PlayerHasPlayedEvend(Player player) {
		super(player);
	}
	
}
