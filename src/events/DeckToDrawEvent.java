package events;

import java.util.EventObject;

public class DeckToDrawEvent extends EventObject {

	public DeckToDrawEvent(Object source) {
		super(source);
	}
	
}
