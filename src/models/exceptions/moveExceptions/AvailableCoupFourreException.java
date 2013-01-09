package models.exceptions.moveExceptions;

import models.cards.HazardCard;
import models.cards.SafetyCard;
import models.players.Player;

public class AvailableCoupFourreException extends Exception {

	// -------------- CONSTANTS -------------- //

	private static final long serialVersionUID = 642524193938322789L;
	
	// -------------- ATTRIBUTES -------------- //
	
	private SafetyCard safety;
	private Player coupFourreInitiator;
	private HazardCard hazardCardInitiator;
	
	// -------------- CONSTRUCTORS -------------- //
	
	public AvailableCoupFourreException( SafetyCard safetyOf, Player initator, HazardCard originCard ) {
		this.safety = safetyOf;
		this.coupFourreInitiator = initator;
		this.hazardCardInitiator = originCard;
	}
	
	// -------------- GETTERS -------------- //

	public HazardCard getHazardCardInitiator() {
		return hazardCardInitiator;
	}

	public Player getCoupFourreInitiator() {
		return coupFourreInitiator;
	}

	public SafetyCard getSafety() {
		return safety;
	}
}
