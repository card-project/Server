package models.players;

import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import listeners.GameEventListener;
import models.cards.Card;
import models.cards.CardFamily;
import models.cards.DistanceCard;
import models.cards.HazardCard;
import models.cards.RemedyCard;
import models.cards.SafetyCard;
import models.exceptions.IllegalCardTypeException;
import models.exceptions.moveExceptions.AvailableCoupFourreException;
import models.stacks.game.DiscardStack;
import models.stacks.game.GameStack;
import models.stacks.player.BattleStack;
import models.stacks.player.DistanceStack;
import models.stacks.player.HandStack;
import models.stacks.player.SafetyStack;
import events.CardDrawnEvent;
import events.ChooseCardAndTargetEvent;
import events.ChooseCardToDiscardEvent;
import events.DeckToDrawEvent;
import events.GameIsOverEvent;
import events.PlayerHasPlayedEvend;

/**
 * 
 * 
 * @version 1.2.4
 * @author Simon RENOULT
 */
public abstract class Player implements Serializable {

	// ------------ ATTRIBUTES ------------ //

	protected int bib; // Each player has a bib
	
	protected String alias;
	protected BattleStack battleStack;
	protected DistanceStack distanceStack;
	protected SafetyStack safetyStack;
	protected HandStack handStack;
	
	protected EventListenerList listeners;

	private static final long serialVersionUID = 8122804038958312079L;	
	
	// ------------ CONSTRUCTORS ------------ //

	public Player(int bib) {
		
		this.bib = bib;
		
		this.handStack = new HandStack();
		this.battleStack = new BattleStack();
		this.safetyStack = new SafetyStack();
		this.distanceStack = new DistanceStack();
		
		this.listeners = new EventListenerList();
	}

	// ------------ METHODS ------------ //

	public void addEventListener(GameEventListener listener) {
		this.listeners.add(GameEventListener.class, listener);
	}
	
	public void removeEventListener(GameEventListener listener) {
		this.listeners.remove(GameEventListener.class, listener);
	}
	
	public Card draw( GameStack drawStackChosen ) {
		drawStackChosen.shiftTopCardTo( this.handStack );
		
		this.fireCardDrawnEvent();
		
		return handStack.peek();
	}
	
	public void fireGameIsOverEvent() {
		
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.gameIsOver(new GameIsOverEvent(this));
		}
	}
	
	public void fireCardDrawnEvent() {
		
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.cardHasBeenDrawn(new CardDrawnEvent(this));
		}
	}
	
	public void fireChooseDeckToDrawEvent() {
		
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.chooseDeckToDraw(new DeckToDrawEvent(this));
		}
	}
	
	public void firePlayCardEvent() {
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.chooseCardToPlay(new ChooseCardAndTargetEvent(this));
		}
	}
	
	
	public void firePlayerHasPlayedEvent() {
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.playerHasPlayed(new PlayerHasPlayedEvend(this));
		}
	}
	
	public void fireDiscardCardEvent() {
		
		GameEventListener[] listenerList = (GameEventListener[]) listeners.getListeners(GameEventListener.class);
		
		for (GameEventListener l : listenerList) {
			l.chooseCardToDiscard(new ChooseCardToDiscardEvent(this));
		}
	}
	

	public boolean play( Card chosenCard, Player target, int distanceGoal ) throws AvailableCoupFourreException {
		boolean replay = false;
		if ( chosenCard instanceof HazardCard ) {
			replay = ( ( HazardCard ) chosenCard ).playOn( this, target );
		} else if ( chosenCard instanceof DistanceCard ) {
			replay = ( ( DistanceCard ) chosenCard ).playOn( target );
		} else if ( chosenCard instanceof RemedyCard ) {
			replay = ( ( RemedyCard ) chosenCard ).playOn( target  );
		} else if ( chosenCard instanceof SafetyCard ) {
			replay = ( ( SafetyCard ) chosenCard ).playOn( target, distanceGoal );
		}
		
		return replay;
	}
	
	public void discard( Card cardToDiscard ) {
		try {
			this.handStack.shiftTo( DiscardStack.getInstance(), cardToDiscard );
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}
	}

	public boolean canPlay( ArrayList<Player> opponents, Integer distanceGoal ) {
		
		boolean canPlay = false;
		
		for( Card handCard : handStack ) {
			if ( ! canPlay ) {
				if ( handCard instanceof DistanceCard ) {
					canPlay = ( ( DistanceCard ) handCard ).isPlayableOn( this, distanceGoal );
				} else if ( handCard instanceof RemedyCard ) {
					canPlay = ( ( RemedyCard ) handCard ).isPlayableOn( this );
				} else if ( handCard instanceof HazardCard ) {
					for( Player opponent : opponents ) {
						if ( ! canPlay ) {
							canPlay = ( ( HazardCard ) handCard ).isPlayableOn( opponent );
						}
					}
				} else if ( handCard instanceof SafetyCard ) {
					canPlay = ( ( SafetyCard ) handCard ).isPlayableOn( this );
				}
			}
		}
		
		return canPlay;
	}

	public boolean isProtectedFrom( HazardCard hc ) {
		for( Card c : safetyStack ) {
			for( CardFamily cf : c.getFamilies() ) {
				if ( cf == hc.getFamily() ) {
					return true;
				}
			}
		}
		
		return false;
	}

	public boolean isAttacked() {
		for ( int i = 0; i < battleStack.size() ; i++) {
			if ( battleStack.get( i ) instanceof HazardCard ) {
				return true;
			}
		}
		
		return false;
	}

	public boolean isSlowed() {
		for ( int i = 0; i < distanceStack.size() ; i++) {
			if ( distanceStack.get( i ).isSpeedLimit() ) {
				return true;
			}
		}
		
		return false;
	}

	public boolean hasStarted() {
		return battleStack.initialGoRollIsPlayed();
	}
	
	public int getTraveledDistance() {
		return this.distanceStack.getTraveledDistance();
	}
	
	@Override
	public String toString() {
		return this.getAlias() + '\n'
				+ "HAND : " + handStack + '\n'
				+ "DISTANCESTACK : " + distanceStack + '\n'
				+ "BATTLESTACK : " + battleStack + '\n'
				+ "SAFETYSTACK : " + safetyStack ;
	}
	
	public boolean canFinish( int gameDistanceGoal ) {
		boolean canFinish = false;
		for ( Card c : handStack ) {
			if ( c instanceof DistanceCard ) {
				if ( ( ( DistanceCard ) c ).getRange() + getTraveledDistance() == gameDistanceGoal ) {
					canFinish = true;
				}
			}
		}
		
		return canFinish;
	}
	
	
	
	// ------------ GETTERS ------------ //

	public BattleStack getBattleStack() {
		return this.battleStack;
	}

	public DistanceStack getDistanceStack() {
		return distanceStack;
	}

	public HandStack getHandStack() {
		return this.handStack;
	}
	
	public SafetyStack getSafetyStack() {
		return safetyStack;
	}

	public void setBattleStack( BattleStack battleStack ) {
		this.battleStack = battleStack;
	}

	public String getAlias() {
		return this.alias;
	}

	public int getBib() {
		return bib;
	}
	
	// ------------ SETTERS ------------ //

	public void setAlias( String string ) {
		this.alias = string;
	}

}
