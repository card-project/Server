package model;

import java.io.Serializable;

public class SimplifiedLounge implements Serializable{

	private int _port;
	private String _name;
	private boolean _private;
	
	public SimplifiedLounge(int port, String name, boolean privated) {
		this.setName(name);
		this.setPrivate(privated);
		this.setPort(port);
	}
	
	
	public int getPort() {
		return _port;
	}
	public void setPort(int _port) {
		this._port = _port;
	}
	public String getName() {
		return _name;
	}
	public void setName(String _name) {
		this._name = _name;
	}
	public boolean isNrivate() {
		return _private;
	}
	public void setPrivate(boolean _private) {
		this._private = _private;
	}
}
