package instruction;

import model.Model;

public class ShareModel extends Instruction {

	private Model _model;
	
	public ShareModel(Model m) {
		this._model = m;
	}
	
	public Model getModel() {
		return _model;
	}
	
	public void setModel(Model _model) {
		this._model = _model;
	}
	
}