package models.cards;

import java.io.Serializable;

import models.Game;
import models.exceptions.IllegalCardTypeException;
import models.players.Player;


/**
 * Distance card "factory".
 * 
 * @author Simon RENOULT
 * @version 1.1.1
 */
public class DistanceCard extends Card implements Serializable {

	// ------------ CONSTANTS ------------ //
	
	// Number of distance card per range value.
	public static final int MAX_25 = 6;
	public static final int MAX_50 = 6;
	public static final int MAX_75 = 6;
	public static final int MAX_100 = 9;
	public static final int MAX_200 = 3;
	
	private static final CardFamily distanceFamily = CardFamily.Speed;
	
	// ------------ ATTRIBUTES ------------ //
	
	private int range;
	private static final long serialVersionUID = -6039625278519431806L;

	// ------------ CONSTRUCTORS ------------ //
	
	/**
	 * Build a new {@link DistanceCard} object depending on its {@link CardType} parameter.
	 * 
	 * @param type {@link CardType} setting the {@link DistanceCard} range value.
	 */
	protected DistanceCard( CardType type ) {
		super( distanceFamily, type );
		try {
			setRange();
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}
	}
	
	// ------------ METHODS ------------ //
	
	/**
	 * Whether the current {@link DistanceCard} is playable on the chosen target.
	 * 
	 * @param p {@link Player} chosen as a target.
	 * @param distanceGoal {@link Game} distance goal.
	 * @return Whether the current {@link DistanceCard} is playable on the chosen target.
	 */
	public boolean isPlayableOn ( Player p, Integer distanceGoal ) {
		if ( ! p.hasStarted() ) {
			return false;
		} else {
			if ( p.isAttacked() ) {
				return false;
			} else if ( p.isSlowed() && this.getRange() > 50 ) {
				return false;
			} else if ( this.getRange() + p.getTraveledDistance() > distanceGoal ) {
				return false;
			} else if ( this.getRange() == 200 && p.getDistanceStack().maxNumberOfDistance200IsReached() ) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Play the current {@link DistanceCard} on the chosen {@link Player} and take it
	 * off from his hand. Determine also whether this move implies the player to replay.
	 * 
	 * @param p {@link Player} who plays the {@link DistanceCard}.
	 * @return Whether the {@link Player} replay after this {@link Card} has been played. 
	 */
	public boolean playOn( Object... args ) {
		Player p = null;
		for ( Object o : args ) {
			if ( o instanceof Player ) {
				p = ( Player ) o;
			} else {
				try {
					throw new IllegalAccessException();
				} catch ( IllegalAccessException e ) {
					e.printStackTrace();
				}
			}
		}
		
		return this.playOn( p );
	};
	
	
	public boolean playOn( Player p ) {
		try {
			p.getHandStack().shiftTo( p.getDistanceStack(), this );
		} catch ( IllegalCardTypeException e ) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return String.valueOf( this.range );
	}
	
	public boolean isAllowedOnSpeedLimit() {
		return this.range <= 50;
	}
	
	// ------------ GETTERS ------------ //
	
	public int getRange() {
		return range;
	}
	
	// ------------ SETTERS ------------ //
	
	/**
	 * Define the correct range value depending on its {@link CardType} attribute.
	 * 
	 * @throws IllegalCardTypeException
	 */
	private void setRange() throws IllegalCardTypeException {
		switch ( super.type ) {
		case Distance25:
			this.range = 25;
			break;
		case Distance50:
			this.range = 50;
			break;
		case Distance75:
			this.range = 75;
			break;
		case Distance100:
			this.range = 100;
			break;
		case Distance200:
			this.range = 200;
			break;
		default:
			throw new IllegalCardTypeException();
		}
	}

	public void setRange( int range ) {
		if ( range == 25 && range == 50 && range == 75 && range == 100 && range == 200 ) {
			throw new IllegalAccessError();
		} else {
			this.range = range;
		}
	}
}
