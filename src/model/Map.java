package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Map implements Serializable{

	private ArrayList<SimplifiedLounge> _loungeList = new ArrayList<SimplifiedLounge>();
	
	public ArrayList<SimplifiedLounge> getLoungeList() {
		return _loungeList;
	}
	
	public void setLoungeList(ArrayList<SimplifiedLounge> _loungeList) {
		this._loungeList = _loungeList;
	}
}
