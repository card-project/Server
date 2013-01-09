package listeners;

import java.util.EventListener;

import events.CardDrawnEvent;
import events.ChooseCardAndTargetEvent;
import events.ChooseCardToDiscardEvent;
import events.DeckToDrawEvent;
import events.GameIsOverEvent;
import events.GameStartedEvent;
import events.PlayerHasPlayedEvend;

public interface GameEventListener extends EventListener {

	public void cardHasBeenDrawn(CardDrawnEvent event);
	public void chooseDeckToDraw(DeckToDrawEvent event);
	public void chooseCardToPlay(ChooseCardAndTargetEvent event);
	public void gameStarted(GameStartedEvent event);
	public void gameIsOver(GameIsOverEvent event);
	public void playerHasPlayed(PlayerHasPlayedEvend event);
	public void chooseCardToDiscard(ChooseCardToDiscardEvent event);
	
}
