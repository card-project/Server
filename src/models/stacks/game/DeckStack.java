package models.stacks.game;

import java.util.Collections;

import models.Game;
import models.cards.Card;
import models.cards.CardFactory;
import models.cards.CardFamily;
import models.cards.DistanceCard;
import models.cards.HazardCard;
import models.cards.RemedyCard;

/**
 * Structure initially containing the whole {@link Game} {@link Card} objects.
 * Use a Singleton design pattern to force the class uniqueness. 
 * 
 * @author Simon RENOULT
 * @version 1.2.1
 */
public class DeckStack extends GameStack {
	
	// ------------ CONSTRUCTORS ------------ //
	
	private static class DeckStackHolder {
		private final static DeckStack INSTANCE = new DeckStack();
	}
	
	private DeckStack() {

		addCard( CardFactory.createDistance( 25 ), DistanceCard.MAX_25 );
		addCard( CardFactory.createDistance( 50 ), DistanceCard.MAX_50 );
		addCard( CardFactory.createDistance( 75 ), DistanceCard.MAX_75 );
		addCard( CardFactory.createDistance( 100 ), DistanceCard.MAX_100 );
		addCard( CardFactory.createDistance( 200 ), DistanceCard.MAX_200 );

		super.cards.add( CardFactory.createSafety( CardFamily.GoStop ) );
		super.cards.add( CardFactory.createSafety( CardFamily.Gas) );
		super.cards.add( CardFactory.createSafety( CardFamily.StateOfCar ) );
		super.cards.add( CardFactory.createSafety( CardFamily.Tire ) );

		addCard( CardFactory.createHazard( CardFamily.Gas ), HazardCard.MAX_OUT_OF_GAS );
		addCard( CardFactory.createHazard( CardFamily.GoStop ), HazardCard.MAX_STOP );
		addCard( CardFactory.createHazard( CardFamily.Speed ), HazardCard.MAX_SPEED_LIMIT );
		addCard( CardFactory.createHazard( CardFamily.StateOfCar ), HazardCard.MAX_ACCIDENT );
		addCard( CardFactory.createHazard( CardFamily.Tire ), HazardCard.MAX_FLAT_TIRE );

		addCard( CardFactory.createRemedy( CardFamily.Gas ), RemedyCard.MAX_GASOLINE );
		addCard( CardFactory.createRemedy( CardFamily.GoStop ), RemedyCard.MAX_GO_ROLL );
		addCard( CardFactory.createRemedy( CardFamily.Speed ), RemedyCard.MAX_END_OF_LIMIT );
		addCard( CardFactory.createRemedy( CardFamily.StateOfCar ), RemedyCard.MAX_REPAIRS );
		addCard( CardFactory.createRemedy( CardFamily.Tire ), RemedyCard.MAX_SPARE_TIRE );
	}

	// ------------ METHODS ------------ //

	private void addCard( Card c, int number ) {
		for ( int i = 0; i < number ; i++ ) {
			super.cards.add( c );
		}
	}
	
	public static DeckStack getInstance() {
		return DeckStackHolder.INSTANCE;
	}
	
	public void shuffle() {
		Collections.shuffle( cards );
	}
}
