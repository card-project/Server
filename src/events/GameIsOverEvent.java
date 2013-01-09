package events;

import java.util.EventObject;

public class GameIsOverEvent extends EventObject {
	
	public GameIsOverEvent(Object source) {
		super(source);
	}

}
