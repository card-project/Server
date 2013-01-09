package models.cards;

/**
 * Families a card can belong to. Allow the creation of cards interacting with
 * same family cards and disabling interactions with different family cards.
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public enum CardFamily {
	GoStop, Speed, Gas, Tire, StateOfCar;

	public CardType getType( Class<DistanceCard> c, int rangeValue ) {
		CardType type = null;
		if ( c.isAssignableFrom( DistanceCard.class ) && this == Speed ) {
			switch ( rangeValue ) {
			case 25:
				type = CardType.Distance25;
				break;
			case 50:
				type = CardType.Distance50;
				break;
			case 75:
				type = CardType.Distance75;
				break;
			case 100:
				type = CardType.Distance100;
				break;
			case 200:
				type = CardType.Distance200;
				break;
			} 
		}
		return type;
	}

	public CardType getType( Class<Card> c ) {
		CardType type = null;

		if ( c.isAssignableFrom( HazardCard.class ) ) {
			switch ( this ) {
			case Gas :
				type = CardType.OutOfGas;
				break;
			case GoStop :
				type = CardType.Stop;
				break;
			case Speed :
				type = CardType.SpeedLimit;
				break;
			case StateOfCar :
				type = CardType.Accident;
				break;
			case Tire :
				type = CardType.FlatTire;
			}
		} else if ( c.isAssignableFrom( RemedyCard.class ) ) {
			switch ( this ) {
			case Gas :
				type = CardType.Gasoline;
				break;
			case GoStop :
				type = CardType.GoRoll;
				break;
			case Speed :
				type = CardType.EndOfLimit;
				break;
			case StateOfCar :
				type = CardType.Repairs;
				break;
			case Tire :
				type = CardType.SpareTire;
				break;
			}
		} else if ( c.isAssignableFrom( SafetyCard.class ) ) {
			switch ( this ) {
			case Gas :
				type = CardType.ExtraTank;
				break;
			case StateOfCar :
				type = CardType.DrivingAce;
				break;
			case Tire :
				type = CardType.PunctureProof;
				break;
			case GoStop :
			case Speed :
				type = CardType.RightOfWay;
				break;
			}
		}

		return type;
	}
}
