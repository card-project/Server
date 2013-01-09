package server.instructions;

import java.util.ArrayList;

public class ConnectedToLounge extends Instruction {
	
	// ------------ ATTRIBUTES ------------ //
	
	private static final long serialVersionUID = -2658169583507680740L;
	private ArrayList<String> players;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public ConnectedToLounge(ArrayList<String> players) {
		this.players = players;
	}
	
	// ------------ GETTERS ------------ //
	
	public ArrayList<String> getPlayers() {
		return players;
	}
	
}