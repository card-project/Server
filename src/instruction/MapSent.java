package instruction;

import model.Map;

public class MapSent extends Instruction{
	
	private Map _map = null;
	
	public MapSent(Map map) {
		this._map = map;
	}
	
	public Map getMap() {
		return this._map;
	}

}
