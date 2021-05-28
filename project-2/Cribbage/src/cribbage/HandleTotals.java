package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class HandleTotals implements ScoringEvent{

    private static Deck deck;
    private static HandleTotals singletonInstance;
    private static CribbageLogger logger;
    private static final int POINTS_FOR_TOTALS = 2;


    private HandleTotals(Deck deck){
        logger = CribbageLogger.getInstance();
        this.deck = deck;
    }

    public static HandleTotals getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandleTotals(deck);
        }
        return singletonInstance;
    }


    @Override
    public int scoreForPlay(Hand cardSet, int playerScore, int playerNum) {
        int totalPlayed = 0;
        for (Card card : cardSet.getCardList()) totalPlayed += Cribbage.cardValue(card);

        if(totalPlayed == 15 || totalPlayed == 31) {
            String number;
            if (totalPlayed == 15)
                number = "fifteen";
            else
                number = "thirtyone";
            logger.log(new Score("P" + playerNum, playerScore + POINTS_FOR_TOTALS, POINTS_FOR_TOTALS, null, number, null));
            playerScore += POINTS_FOR_TOTALS;
        }
        return playerScore;
    }

    @Override
    public int scoreForShow(Hand cardSet, int playerScore, int playerNum) {
        return playerScore;
    }
}
