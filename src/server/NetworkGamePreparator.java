package server;

import javax.swing.event.EventListenerList;

import models.Game;
import models.exceptions.AliasAlreadyChosenException;
import models.exceptions.IllegalDistanceException;
import models.exceptions.IllegalHumanPlayerNumberException;
import models.exceptions.IllegalPlayerNumberException;
import models.players.HumanPlayer;
import server.thread.Lounge;

public class NetworkGamePreparator {

	// ------------ ATTRIBUTES ------------ //
	
	private Game model;
	private Lounge lounge;
	
	private final int FINALGOAL = 1000;
	
	// ------------ CONSTRUCTORS ------------ //
	
	public NetworkGamePreparator(Game model, Lounge lounge) {
		this.model = model;
		this.lounge = lounge;
	}
	
	// ------------ METHODS ------------ //
	
	/**
	 * Start the initialization process. 
	 */
	public void execute() {
		
		this.initTotalPlayers();
		this.initHumanPlayer();
		this.defineFinalGoal();
		this.definePlayersAlias();
		this.model.setAIPlayersAlias();
		this.model.initiateAIPlayers();
		
		this.prepareDecks();
	}
	
	/**
	 * Shuffle and distribute cards to players.
	 */
	private void prepareDecks() {
		this.model.getDeckStack().shuffle();
		this.model.distributeCardsToPlayers();
	}
	
	/**
	 * Init the total count of players.
	 */
	public void initTotalPlayers() {
		try {
			this.model.setPlayersNumber(this.lounge.getTotalPlaces());
		} catch (IllegalPlayerNumberException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Init a single human player.
	 */
	public void initHumanPlayer() {
		try {
			this.model.setHumanPlayersNumber(this.lounge.getTotalPlaces());
		} catch (IllegalHumanPlayerNumberException e) {
			e.printStackTrace();
		}
	}
	
	public void defineFinalGoal() {
		try {
			this.model.setDistanceGoal (FINALGOAL);
		} catch ( IllegalDistanceException e ) {}
	}
	
	public void definePlayersAlias() {
		
		for ( int i = 0; i < this.model.getPlayers().length ; i++ ) {
			if ( this.model.getPlayers()[i] instanceof HumanPlayer ) {
				
				try {
					this.model.setPlayerAlias( this.model.getPlayers()[i], this.lounge.getPlayersList(i));
				} catch ( AliasAlreadyChosenException e ) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
