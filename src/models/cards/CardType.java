package models.cards;

import java.util.ArrayList;

/**
 * Type a {@link Card} can have.
 * 
 * Exclusively used to get the card name
 * 
 * @author Simon RENOULT
 * @version 1.0
 */
public enum CardType {
	
	// Distances
	Distance25, Distance50, Distance75, Distance100, Distance200, 
	// Hazards
	Accident, FlatTire, OutOfGas, SpeedLimit, Stop,
	// Remedies
	Repairs, SpareTire, Gasoline, EndOfLimit, GoRoll,
	// Safeties
	DrivingAce, PunctureProof, ExtraTank, RightOfWay;
	
	public ArrayList<CardFamily> getFamilies() {
		ArrayList<CardFamily> families = new ArrayList<CardFamily>();
		
		if ( this == Distance25 || this == Distance50 || this == Distance75 || this == Distance100 || this == Distance200 ) {
			families.add( CardFamily.Speed );
		}
		
		if ( this == Accident || this == Repairs || this == DrivingAce ) {
			families.add( CardFamily.StateOfCar );
		}
		
		if ( this == FlatTire || this == SpareTire || this == PunctureProof ) {
			families.add( CardFamily.Tire );
		}
		
		if ( this == OutOfGas || this == Gasoline || this == ExtraTank ) {
			families.add( CardFamily.Gas );
		}
		
		if ( this == Stop || this == GoRoll || this == RightOfWay ) {
			families.add( CardFamily.GoStop );
		}
		
		if ( this == SpeedLimit || this == EndOfLimit || this == RightOfWay ) {
			families.add( CardFamily.Speed );
		} 
		
		return families;
	}
}
