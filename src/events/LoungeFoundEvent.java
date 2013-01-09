package events;

import java.util.EventObject;

public class LoungeFoundEvent extends EventObject {

	// ------------ ATTRIBUTES ------------ //
	private static final long serialVersionUID = 8918102710581040863L;
	private String ipAdress;
	private String loungeName;
	private String port;
	
	// ------------ CONSTRUCTORS ------------ //
	public LoungeFoundEvent(Object source, String ipAdress, String port, String loungeName) {
		super(source);
		
		this.port = port;
		this.ipAdress = ipAdress;
		this.loungeName = loungeName;
	}
	
	// ------------ GETTERS ------------ //
	public String getIpAdress() {
		return ipAdress;
	}
	
	public String getLoungeName() {
		return loungeName;
	}
	
	public String getPort() {
		return port;
	}
}
