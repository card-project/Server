package server.instructions;

import models.Game;

public class GameStarted extends Instruction {
	

	// ------------ ATTRIBUTES ------------ //
	
	private Game _model;
	private static final long serialVersionUID = 6247949725810745775L;	
	
	// ------------ CONSTRUCTORS ------------ //
	
	public GameStarted(Game m) {
		this._model = m;
	}
	
	// ------------ GETTERS ------------ //
	
	public Game getModel() {
		return _model;
	}
	
	// ------------ SETTERS ------------ //
	
	public void setModel(Game _model) {
		this._model = _model;
	}

}
