package models.cards;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * <p>Create an abstract default Card having a type and some families.
 * <br />
 * This type is used to get the card name as same family cards
 * can be different depending on the Card subclasses.
 * <br />
 * The family type is used to recognize cards inferring on
 * the same theme : {@link CardFamily}.</p>
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public abstract class Card implements Serializable {
	
	// ------------ ATTRIBUTES ------------ //
	
	protected ArrayList<CardFamily> families = null;
	protected CardType type = null;
	private static final long serialVersionUID = -6300647029223207777L;
	
	// ------------ CONSTRUCTORS ------------ //
	
	/**
	 * Create a new Card object.
	 * 
	 * Set its initial family and its cartType (exclusively used to get the card name). 
	 * 
	 * @param initialFamily Initial card family.
	 * @param cardType Type of the created card.
	 */
	protected Card( CardFamily initialFamily, CardType cardType ) {
		this.families = new ArrayList<CardFamily>();
		this.addFamily( initialFamily );
		
		this.type = cardType;
	}

	protected Card( CardType cardType ) {
		this.type = cardType;
		this.families = new ArrayList<CardFamily>();
		this.setFamilies();
	}
	
	protected Card( CardFamily initialFamily ) {
		this.families = new ArrayList<CardFamily>();
		this.addFamily( initialFamily );
		this.setType();
	}
	
	// ------------ METHODS ------------ //
	
	/**
	 * Add a family to the current card.
	 * 
	 * @param family {@link CardFamily} to add.
	 */
	public void addFamily( CardFamily family ) {
		this.families.add( family );
	}

	@Override
	public String toString() {
		return this.type.toString();
	}
	
	/**
	 * Whether the card is a GoRoll.
	 * 
	 * @return Whether the card is a GoRoll.
	 */
	public boolean isGoRoll() {
		boolean isGoStopFamily = false;
		for( CardFamily cf : this.getFamilies() ) {
			if( cf == CardFamily.GoStop ) {
				isGoStopFamily = true;
			}
		}
		
		return this instanceof RemedyCard && isGoStopFamily ;
	}
	
	/**
	 * Whether the card is a SpeedLimit.
	 * 
	 * @return Whether the card is a SpeedLimit.
	 */
	public boolean isSpeedLimit() {
		boolean isSpeedFamily = false;
		for( CardFamily cf : this.getFamilies() ) {
			if ( cf == CardFamily.Speed ) {
				isSpeedFamily = true;
			}
		}
		
		return this instanceof HazardCard && isSpeedFamily;
	}
	
	/**
	 * Whether the card is a RightOfWay.
	 * 
	 * @return Whether the card is a RightOfWay.
	 */
	public boolean isRightOfWay() {
		boolean isSpeedOrGoStopFamily = false;
		for( CardFamily cf : this.getFamilies() ) {
			if ( cf == CardFamily.Speed || cf == CardFamily.GoStop ) {
				isSpeedOrGoStopFamily = true;
			}
		}
		
		return this instanceof HazardCard && isSpeedOrGoStopFamily;
	}
	
	/**
	 * Whether the current card can make an other card pop from the stack it belongs to.
	 * 
	 * @param hazardFamily Family card to test.
	 * @return Whether the current Card counteract the parameter's family.
	 */
	public boolean counteract( CardFamily hazardFamily ) {
		if ( this instanceof RemedyCard || this instanceof SafetyCard ) {
			for ( CardFamily cf : this.getFamilies() ) {
				if ( cf == hazardFamily ) {
					return true;
				}
			}
		}

		return false;
	}
	
	// -------------- SETTERS -------------- //
	
	private void setFamilies() {
		if ( this.type == null ) {
			throw new IllegalAccessError();
		} else {
			this.families = type.getFamilies();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setType() {
		if ( this.families == null ) {
			throw new IllegalAccessError();
		} else {
			if ( this instanceof DistanceCard ) {
				this.type = families.get( 0 ).getType( ( Class<DistanceCard> ) this.getClass(), ( ( DistanceCard ) this ).getRange() );
			} else {
				this.type = families.get( 0 ).getType( ( Class<Card> ) this.getClass() );
			}
		}
	}
	
	// ------------ GETTERS ------------ //
	
	public CardFamily getFamily() {
		return this.getFamilies().get( 0 );
	}
	
	public ArrayList<CardFamily> getFamilies() {
		return this.families;
	}
	
	public CardType getType() {
		return this.type;
	}
}
