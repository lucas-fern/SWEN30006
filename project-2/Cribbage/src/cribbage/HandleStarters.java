package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class HandleStarters implements ScoringEvent{

    private final Deck deck;
    private static HandleStarters singletonInstance;
    private static CribbageLogger logger;
    private boolean starterCardScored = false;

    private HandleStarters(Deck deck){
        logger = CribbageLogger.getInstance();
        this.deck = deck;
    }

    public static HandleStarters getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandleStarters(deck);
        }
        return singletonInstance;
    }

    @Override
    public int scoreForPlay(Hand cardSet, int playerScore, int playerNum) {
        if(!starterCardScored && "J".equals(cardSet.getFirst().getRank().toString())){
            logger.log(new Score("P1", playerScore+2, 2, null, "starter", null));
            starterCardScored = true;
            playerScore += 2;
        }
        return playerScore;
    }

    @Override
    public int scoreForShow(Hand cardSet, int playerScore, int playerNum) {
        Card starter = cardSet.getFirst().clone();
        for(Card card : cardSet.getCardList()){
            if(card.getRank().toString().equals("JACK") && card.getSuit() == starter.getSuit()) {
                Hand hand = new Hand(deck);
                hand.insert(card, false);
                logger.log(new Score("P" + playerNum, playerScore + 1, 1, null, "jack", hand));
                playerScore += 1;
            }
        }
        return playerScore;
    }
}
