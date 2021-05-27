package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import static java.lang.Integer.min;

import java.util.ArrayList;


public class HandleRuns implements ScoringEvent {

    private static Deck deck;
    private static HandleRuns singletonInstance;

    private static final int LONGEST_RUN = 7;
    private static final int SHORTEST_RUN = 3;

    private HandleRuns(Deck deck){
        this.deck = deck;
    }

    public static HandleRuns getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandleRuns(deck);
        }
        return singletonInstance;
    }

    @Override
    public int scoreForPlay(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        ArrayList<Card> handCards = cardSet.getCardList();
        Hand cardsToSearch = new Hand(deck);
        if(handCards.size() >= 2) {
            cardsToSearch.insert(handCards.get(handCards.size() - 1), false);
            cardsToSearch.insert(handCards.get(handCards.size() - 2), false);
        }

        int i = SHORTEST_RUN;

        while(i<=min(LONGEST_RUN, handCards.size())){
            cardsToSearch.insert(handCards.get(handCards.size() - i), false);
            playerScore = run_of_size(cardsToSearch, i, event, false, playerScore, playerNum);
            i+= 1;
        }

        return playerScore;
    }

    @Override
    public int scoreForShow(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        int i = SHORTEST_RUN;
        while(i<=min(LONGEST_RUN, cardSet.getNumberOfCards())){
            playerScore = run_of_size(cardSet, i, event, true, playerScore, playerNum);
            i += 1;
        }

        return playerScore;
    }

    private int run_of_size(Hand cardsToSearch, int runLength, CribbageEvent event, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractSequences(runLength)){
            if (displayCards) {
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + runLength, runLength, null, "run" + runLength, hand));
            }
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore+runLength, runLength, null, "run" + runLength, null));
            playerScore += runLength;
        }
        return playerScore;
    }
}
