package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class HandleTotals implements ScoringEvent{

    private static Deck deck;
    private static HandleTotals singletonInstance;

    private HandleTotals(Deck deck){
        this.deck = deck;
    }

    public static HandleTotals getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandleTotals(deck);
        }
        return singletonInstance;
    }


    @Override
    public int scoreForPlay(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        int totalPlayed = 0;
        for (Card card : cardSet.getCardList()) totalPlayed += Cribbage.cardValue(card);

        if(totalPlayed == 15 || totalPlayed == 31) {
            String number;
            if (totalPlayed == 15)
                number = "fifteen";
            else
                number = "thirtyone";
            Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 2, 2, null, number, null));
            playerScore += 2;
        }
        return playerScore;
    }

    @Override
    public int scoreForShow(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        return playerScore;
    }
}
