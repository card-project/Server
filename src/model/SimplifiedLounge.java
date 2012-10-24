package model;

import java.io.Serializable;

public class SimplifiedLounge implements Serializable{

	private String _name;
	private boolean _private;
	
	public SimplifiedLounge(String name, boolean privated) {
		this.setName(name);
		this.setPrivate(privated);
	}
	
	
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public boolean isPrivate() {
		return _private;
	}
	public void setPrivate(boolean _private) {
		this._private = _private;
	}
}
