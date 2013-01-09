package server.instructions;

public class JoinLounge extends Instruction{
	
	// ------------ ATTRIBUTES ------------ //
	
	private String _loungeName;
	private String _password;
	private static final long serialVersionUID = 2891638913737103263L;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public JoinLounge(String loungeName, String password) {
		this(loungeName);
		this._password = password;
	}
	
	public JoinLounge(String loungeName) {
		this._loungeName = loungeName;
	}
	
	// ------------ GETTERS ------------ //
	
	public String getLoungeName() {
		return _loungeName;
	}
	
	public String getPassword() {
		return _password;
	}
	
	// ------------ SETTERS ------------ //
	
	public void setPassword(String _password) {
		this._password = _password;
	}
}
