package events;

import java.util.EventObject;

public class ChooseCardAndTargetEvent extends EventObject {

	private static final long serialVersionUID = 2729060653236825152L;

	public ChooseCardAndTargetEvent(Object source) {
		super(source);
	}
	
}
