package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class HandleFlushes implements ScoringEvent {

    private static HandleFlushes singletonInstance;
    private static CribbageLogger logger;

    private HandleFlushes(){
        logger = CribbageLogger.getInstance();
    }

    public static HandleFlushes getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new HandleFlushes();
        }
        return singletonInstance;
    }


    @Override
    public int scoreForPlay(Hand cardSet, int playerScore, int playerNum) {
        return playerScore;
    }

    @Override
    public int scoreForShow(Hand cardSet, int playerScore, int playerNum) {
        Card starter = cardSet.getFirst();
        cardSet.removeFirst(false);
        for (Cribbage.Suit suit : Cribbage.Suit.values()){
            if(cardSet.getCardsWithSuit(suit).size() == 4){
                if(starter.getSuit() == suit) {
                    cardSet.insert(starter, false);
                    logger.log(new Score("P" + playerNum, playerScore + 5, 5, null, "flush5", cardSet));
                    playerScore += 5;
                }
                else {
                    logger.log(new Score("P" + playerNum, playerScore + 4, 4, null, "flush4", null));
                    playerScore += 4;
                }
            }

        }
        return playerScore;
    }
}