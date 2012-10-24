package instruction;

public class JoinLounge extends Instruction{

	private String _loungeName;
	private String _password;
	
	public JoinLounge(String loungeName, String password) {
		this(loungeName);
		this._password = password;
	}
	
	public JoinLounge(String loungeName) {
		this._loungeName = loungeName;
	}
	
	public String getLoungeName() {
		return _loungeName;
	}
	
	public String getPassword() {
		return _password;
	}
	
	public void setPassword(String _password) {
		this._password = _password;
	}
}
