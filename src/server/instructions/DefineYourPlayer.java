package server.instructions;

import models.players.Player;

public class DefineYourPlayer extends Instruction {

	// ------------ ATTRIBUTES ------------ //	
	private Player player;
	private static final long serialVersionUID = 4980538934971120807L;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public DefineYourPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
}
