package server.instructions;

import models.Game;

public class ShareModel extends Instruction {

	// ------------ ATTRIBUTES ------------ //
	
	private static final long serialVersionUID = -5304124568689089870L;
	private Game _model;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public ShareModel(Game m) {
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