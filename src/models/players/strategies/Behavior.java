package models.players.strategies;

import java.io.Serializable;

import models.players.AIPlayer;

public abstract class Behavior implements Strategy, Serializable{

	// -------------- ATTRIBUTES -------------- //
	
	private static final long serialVersionUID = -6357219457855430986L;
	protected AIPlayer owner;

	// -------------- CONSTRUCTORS -------------- //
	
	public Behavior( AIPlayer owner ) {
		super();
		this.owner = owner;
	}
	
	// -------------- METHODS -------------- //
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName().toString();
	}
}
