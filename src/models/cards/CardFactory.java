package models.cards;

import java.io.Serializable;

/**
 * Allow lighter/softer dependencies between classes.
 * Create a new card object depending on the parameter passed.
 * 
 * @author Simon RENOULT
 * @version 1.2.1
 */
public abstract class CardFactory implements Serializable{
	
	// ------------ ATTRIBUTES ------------ //
	private static final long serialVersionUID = 6701765241429142626L;

	/**
	 * Return a new {@link HazardCard}. Its family depends on the family argument.
	 * 
	 * @param cf {@link CardFamily} Chosen hazard family.
	 * @return A new {@link HazardCard} object.
	 */
	public static HazardCard createHazard( CardFamily cf ) {
		switch ( cf ) {
		case Gas: 
			return new HazardCard( cf, CardType.OutOfGas );
		case GoStop: 
			return new HazardCard( cf, CardType.Stop );
		case Speed: 
			return new HazardCard( cf, CardType.SpeedLimit );
		case StateOfCar: 
			return new HazardCard( cf, CardType.Accident);
		case Tire: 
			return new HazardCard( cf, CardType.FlatTire );
		}
		
		return null;
	}

	/**
	 * Return a new {@link RemedyCard}. Its family depends on the family argument.
	 * 
	 * @param cf {@link CardFamily} Chosen hazard family.
	 * @return A new {@link RemedyCard} object.
	 */
	public static RemedyCard createRemedy( CardFamily cf ) {
		switch ( cf ) {
		case Gas: 
			return new RemedyCard( cf, CardType.Gasoline );
		case GoStop: 
			return new RemedyCard( cf, CardType.GoRoll);
		case Speed: 
			return new RemedyCard( cf, CardType.EndOfLimit );
		case StateOfCar: 
			return new RemedyCard( cf, CardType.Repairs );
		case Tire: 
			return new RemedyCard( cf, CardType.SpareTire );
		}
		
		return null;
	}

	/**
	 * Return a new {@link DistanceCard}. Its family depends on the family argument.
	 * 
	 * @param cf {@link CardFamily} Chosen hazard family.
	 * @return A new {@link DistanceCard} object.
	 */
	public static DistanceCard createDistance( int value ) {
		switch ( value ) {
		case 25: 
			return new DistanceCard( CardType.Distance25 );
		case 50: 
			return new DistanceCard( CardType.Distance50 );
		case 75: 
			return new DistanceCard( CardType.Distance75 );
		case 100: 
			return new DistanceCard( CardType.Distance100 );
		case 200: 
			return new DistanceCard( CardType.Distance200 );
		}
		
		return null;
	}

	/**
	 * Return a new {@link SafetyCard}. Its family depends on the family argument.
	 * 
	 * @param cf {@link CardFamily} Chosen hazard family.
	 * @return A new {@link SafetyCard} object.
	 */
	public static SafetyCard createSafety( CardFamily cf ) {
		switch ( cf ) {
		case Gas: 
			return new SafetyCard( cf, CardType.ExtraTank );
		case GoStop: 
			SafetyCard sc1 = new SafetyCard( cf, CardType.RightOfWay );
			sc1.addFamily( CardFamily.Speed );
			return sc1;
		case Speed: 
			SafetyCard sc2 = new SafetyCard( cf, CardType.RightOfWay );
			sc2.addFamily( CardFamily.GoStop );
			return sc2;
		case StateOfCar: 
			return new SafetyCard( cf, CardType.DrivingAce );
		case Tire: 
			return new SafetyCard( cf, CardType.PunctureProof );
		}
		
		return null;
	}
}
