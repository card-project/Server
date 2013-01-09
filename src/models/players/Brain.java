package models.players;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import models.cards.Card;
import models.players.strategies.Driver;
import models.players.strategies.Protector;
import models.players.strategies.Roadhog;
import models.players.strategies.Strategy;
import models.stacks.game.DeckStack;
import models.stacks.game.GameStack;


public class Brain implements Strategy, Iterable<Strategy>, Serializable {

	// -------------- CONSTANTS -------------- //
	
	private static final int CRITICAL_DISTANCE = 300;
	
	// ------------ ATTRIBUTES ------------ //

	private LinkedList<Strategy> mind = new LinkedList<Strategy>();
	private AIPlayer owner;
	private Integer distanceGoal;
	private ArrayList<Player> opponents;
	
	private static final long serialVersionUID = 867853853096479509L;
	
	// ------------ CONSTRUCTORS ------------ //
	
	/**
	 * Create a new brain with different organized strategies.
	 * Their priority depends on their list index.
	 * 
	 * @param owner {@link Player} owning the current {@link Brain}.
	 * @param opponents {@link ArrayList} of {@link Player} defining the other players.
	 * @param distanceGoal 
	 */
	public Brain( AIPlayer owner, ArrayList<Player> opponents, Integer distanceGoal ) {
		this.owner = owner;
		this.opponents = opponents;
		this.distanceGoal = distanceGoal;
		
		initStrategies();
	}
	
	// ------------ METHODS ------------ //
	
	private void initStrategies() {
		switch ( new Random().nextInt( 3 ) ) {
		case 0:
			this.mind.add( new Driver( this.owner, distanceGoal ) );
			this.mind.add( new Protector( this.owner, opponents ) );
			this.mind.add( new Roadhog( this.owner, opponents ) );
			break;
		case 1:
			this.mind.add( new Roadhog( this.owner, opponents ) );
			this.mind.add( new Driver( this.owner, distanceGoal ) );
			this.mind.add( new Protector( this.owner, opponents ) );
			break;
		case 2:
			this.mind.add( new Driver( this.owner, distanceGoal ) );
			this.mind.add( new Protector( this.owner, opponents ) );
			this.mind.add( new Roadhog( this.owner, opponents ) );
			break;
		}
	}
	
	private void updateStrategies() {
		if ( ! owner.canFinish( distanceGoal ) ) {
			for ( Player opp : opponents ) {
				if ( opp.getTraveledDistance() + CRITICAL_DISTANCE >= distanceGoal ) {
					for ( int i = 0; i < mind.size() ; i++ ) {
						if ( mind.get( i ) instanceof Roadhog ) {
							this.mind.remove( this.mind.lastIndexOf( mind.get( i ) ) );
							this.mind.push( new Roadhog( owner, opponents ) );
						}
					}
				}
			}
		}
	}
	
	@Override
	public Iterator<Strategy> iterator() {
		return this.mind.iterator();
	}
	
	@Override
	public GameStack chooseStackToDraw() {
		this.updateStrategies();
		
		GameStack chosenStack = null;
		for( Strategy s : this.mind ) {
			if ( chosenStack == null ) {
				chosenStack = s.chooseStackToDraw();
				if ( s.chooseStackToDraw() != null ) {
					System.out.println( s );
					System.out.println( s.chooseStackToDraw().getClass().getSimpleName() );
				}
			}
		}
		
		if ( chosenStack == null ) {
			chosenStack =  DeckStack.getInstance();
		}
		
		
		return chosenStack;
	}

	@Override
	public Card chooseCardToPlay() {
		Card chosenCard = null;
		
		if ( this.owner.canPlay( opponents, this.distanceGoal ) ) {
			for( Strategy s : this.mind ) {
				if ( chosenCard == null ) {
					chosenCard = s.chooseCardToPlay();
				}
			}
		}
		
		return chosenCard;
	}

	@Override
	public Card chooseCardToDiscard() {
		Card cardToDiscard = null;
		
		for ( int i = this.mind.size() - 1 ; i >= 0 ; i-- ) {
			if ( cardToDiscard == null ) {
				cardToDiscard = this.mind.get( i ).chooseCardToDiscard();
			}
		}
		
		if ( cardToDiscard == null ) {
			int randomIndex = new Random().nextInt( this.owner.getHandStack().size() );
			cardToDiscard = this.owner.getHandStack().get( randomIndex );
		}
		
		return cardToDiscard;
	}

	public Player chooseTargetToAttack() {
		Player chosenTarget = null;
		for( Strategy s : this.mind ) {
			if ( s instanceof Roadhog ) {
				chosenTarget = ( ( Roadhog ) s ).chooseTargetToAttack();
			}
		}
		
		return chosenTarget;
	}
	
	// -------------- GETTERS -------------- //
	
	public LinkedList<Strategy> getMind() {
		return mind;
	}
	
	public Integer getDistanceGoal() {
		return distanceGoal;
	}
	
	public ArrayList<Player> getOpponents() {
		return opponents;
	}
}
