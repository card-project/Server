package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Model implements Serializable{

	private ArrayList<String> list;
	
	public Model() {
		this.list = new ArrayList<String>();
	}
	
	public Model(String s) {
		this();
		this.list.add(s);
	}
	
	public ArrayList<String> getList() {
		return list;
	}
	
	public void setList(ArrayList<String> list) {
		this.list = list;
	}
}
