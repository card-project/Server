package models.players;

/**
 * Human Player object.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public class HumanPlayer extends Player {
	
	// ------------ CONSTRUCTORS ------------ //
	
	public HumanPlayer(int bib) {
		super(bib);
	}
	
	// ------------ METHODS ------------ //
	
	public void draw() {
		this.fireChooseDeckToDrawEvent();
	}

}
