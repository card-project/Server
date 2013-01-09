package events;

import java.util.EventObject;

public class CancelChosenCardEvent extends EventObject {
	public CancelChosenCardEvent(Object source) {
		super(source);
	}
}
