package models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import listeners.GameEventListener;
import models.cards.HazardCard;
import models.exceptions.AliasAlreadyChosenException;
import models.exceptions.IllegalDistanceException;
import models.exceptions.IllegalHumanPlayerNumberException;
import models.exceptions.IllegalPlayerNumberException;
import models.players.AIPlayer;
import models.players.HumanPlayer;
import models.players.Player;
import models.stacks.game.DeckStack;
import models.stacks.game.DiscardStack;
import models.stacks.player.HandStack;
import events.GameStartedEvent;

/**
 * @version 0.2.2
 * 
 * TODO : when deck is empty, turn discard stack into deck and shuffle
 * 
 * @author Simon RENOULT
 */
public class Game implements Serializable {

	// ------------ CONSTANTS ------------ //

	private static final long serialVersionUID = 3775686336877562477L;
	public static final int MIN_PLAYERS_NB = 2;
	public static final int MAX_PLAYERS_NB = 4;
	// Allow IA vs IA game.
	public static final int MIN_HUMAN_PLAYERS_NB = 0;
	public static final int MIN_DISTANCE_GOAL = 700;
	public static final int MAX_DISTANCE_GOAL = 1000;
	public static final int MIN_AI_LEVEL = 1;
	public static final int MAX_AI_LEVEL = 3;
	
	// ------------ ATTRIBUTES ------------ // 
	
	private Integer goal;
	private Player[] players;
	private DiscardStack discardStack;
	private DeckStack deckStack;
	
	private int currentPlayerIndex;
	
	private EventListenerList listeners = new EventListenerList();

	// ------------ CONSTRUCTORS ------------ // 

	public Game() {
		this.deckStack = DeckStack.getInstance();
		this.discardStack = DiscardStack.getInstance();
	}
	
	// ------------ METHODS ------------ //
	
	public void startGame() {
		this.setCurrentPlayer(this.getFirstPlayerIndexAsRandom());
		this.fireGameStartedEvent(this);
	}
	
	public void setCurrentPlayer(int firstPlayerIndexAsRandom) {
		this.currentPlayerIndex = firstPlayerIndexAsRandom;
	}
	
	public Player getCurrentPlayer() {
		return this.players[this.currentPlayerIndex];
	}

	private int getFirstPlayerIndexAsRandom() {
		return (int) ( Math.random() * ( this.getPlayers().length ) );
	}
	
	public void distributeCardsToPlayers() {
		// Draw initial hand (4 cards) for each player.
		for ( Player p : players ) {
			for ( int i = 0; i < HandStack.MAX_CARD_NB; i++ ) {
				p.draw( this.deckStack );
			}
		}
	}
	
	public String toString() {
		String players = "";
		
		for ( Player p : this.players ){
			players += p.toString();
		}
		
		return "Goal : " + this.goal + '\n' +
				"Players : " + players  + '\n' +
				"DiscardStack " + this.discardStack + '\n' +
				"DeckStack" + this.deckStack;
	}
	
	// ------------ SETTERS ------------ // 

	public void setPlayersNumber( int playersNumber ) throws IllegalPlayerNumberException {
		if ( playersNumber < MIN_PLAYERS_NB || playersNumber > MAX_PLAYERS_NB ) {
			throw new IllegalPlayerNumberException();
		} else {
			this.players = new Player [playersNumber];
		}
	}

	public void setHumanPlayersNumber( int humanPlayersNumber ) throws IllegalHumanPlayerNumberException {
		if ( humanPlayersNumber < MIN_HUMAN_PLAYERS_NB || humanPlayersNumber > this.players.length ) {
			throw new IllegalHumanPlayerNumberException();
		} else {
			for ( int i = 0; i < this.players.length; i++ ) {
				this.players[i] = ( i < humanPlayersNumber ) ? new HumanPlayer(i) : new AIPlayer(i);
			}
		}
	}	
	
	public void setDistanceGoal( int distanceGoal ) throws IllegalDistanceException {
		if ( ( distanceGoal < MIN_DISTANCE_GOAL || distanceGoal > MAX_DISTANCE_GOAL || distanceGoal%25 != 0 ) ) {
			throw new IllegalDistanceException();
		} else {
			this.goal = distanceGoal;
		}
	}

	public void setPlayerAlias( Player player, String askedPlayerAlias ) throws AliasAlreadyChosenException  {
		for ( Player p : this.players ) {
			if ( player != p ) {
				if ( p.getAlias() == askedPlayerAlias ) {
					throw new AliasAlreadyChosenException( "Alias already chosen." );
				}
			}
		}
		
		player.setAlias( askedPlayerAlias );
	}
	
	public void setAIPlayersAlias() {
		String[] aiAliases = { "ann", "bob", "lea", "dan", "emile", "fanny" };
		
		for ( int i = 0; i < this.players.length; i++ ) {
			if ( players[i] instanceof AIPlayer ) {
				Boolean aliasIsAlreadyChosen;
				do {
					try {
						aliasIsAlreadyChosen = false;
						this.setPlayerAlias( players[i], aiAliases[(int) ( Math.random() * aiAliases.length )] );
					} catch ( AliasAlreadyChosenException e ) {
						aliasIsAlreadyChosen = true;
					}
				} while ( aliasIsAlreadyChosen );
			}
		}
	}
	
	public void initiateAIPlayers() {
		for ( AIPlayer ai : this.getAIPlayers() ) {
			ai.makeMeClever( this.getOpponents( ai ), this.goal );
		}
	}
	
	// ------------ GETTERS------------ // 

	public ArrayList<Player> getOpponents( Player ignoredPlayer ) {
		ArrayList<Player> opponents = new ArrayList<Player>();
		for ( Player p : this.players ) {
			if ( ! p.equals( ignoredPlayer ) ) {
				opponents.add( p );
			}
		}
		
		return opponents;
	}
	
	public ArrayList<Player> getAttackableOpponents( Player ignoredPlayer, HazardCard c ) {
		ArrayList<Player> opponents = getOpponents( ignoredPlayer );
		
		for ( int i = 0; i < opponents.size(); i++ ) {
			if ( ! c.isPlayableOn( opponents.get( i ) ) ) {
				opponents.remove( i );
			}
		}
		
		return opponents;
	}
	
	public int getIndexOf( Player searchedPlayer ) {
		for ( int i = 0; i < this.players.length; i++ ) {
			if ( this.players[i].equals( searchedPlayer ) ) {
				return i;
			}
		}
		
		return -1;
	}
	
	public Player[] getPlayers() {
		return this.players;
	}

	public AIPlayer[] getAIPlayers() {
		ArrayList<AIPlayer> aiPlayers = new ArrayList<AIPlayer>();
		for ( Player p : players ) {
			if ( p instanceof AIPlayer ) {
				aiPlayers.add( (AIPlayer) p );
			}
		}
		
		return aiPlayers.toArray( new AIPlayer[aiPlayers.size()] );
	}
	
	public HumanPlayer[] getHumanPlayers() {
		ArrayList<HumanPlayer> humanPlayers = new ArrayList<HumanPlayer>();
		for ( Player p : players ) {
			if ( p instanceof HumanPlayer) {
				humanPlayers.add( (HumanPlayer) p );
			}
		}
		
		return humanPlayers.toArray( new HumanPlayer[humanPlayers.size()] );
	}
	
	public DeckStack getDeckStack() {
		return this.deckStack;
	}

	public DiscardStack getDiscardStack() {
		return this.discardStack;
	}

	public Integer getGoal() {
		return this.goal;
	}

	public boolean nextPlayer() {
		
		boolean gameIsOver = this.gameIsOver();
		
		if ( ! gameIsOver ) {
			currentPlayerIndex = ( ++currentPlayerIndex > this.getPlayers().length - 1 ) ? 0 : currentPlayerIndex ;
		}
		else
			this.fireGameIsOverEvent();
		
		return gameIsOver;
	}
	
	public boolean gameIsOver() {
		return this.getCurrentPlayer().getTraveledDistance() == this.getGoal();
	}
	
// ------------ EVENT && LISTENERS ------------ //
	
	public void addEventListener(GameEventListener listener) {
		for (Player p : this.players) {
			p.addEventListener(listener);
		}
		
		this.listeners.add(GameEventListener.class, listener);
	}
	
	public void removeEventListener(GameEventListener listener) {
		for (Player p : this.players) {
			p.removeEventListener(listener);
		}
		
		this.listeners.remove(GameEventListener.class, listener);
	}
	
	private void fireGameIsOverEvent() {
		for (Player p : this.players) {
			p.fireGameIsOverEvent();
		}
	}
	
	private void fireGameStartedEvent(Game game) {
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.gameStarted(new GameStartedEvent(game));
		}
	}

	public void firePlayCardEvent() {
		this.getCurrentPlayer().firePlayCardEvent();
	}

	public void fireDiscardCardEvent() {
		this.getCurrentPlayer().fireDiscardCardEvent();
	}

}
