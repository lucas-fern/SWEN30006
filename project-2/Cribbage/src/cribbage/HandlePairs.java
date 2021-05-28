package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class HandlePairs implements ScoringEvent{

    private static Deck deck;
    private static HandlePairs singletonInstance;

    private static final int PAIR_SCORE = 2;
    private static final int TRIP_SCORE = 6;
    private static final int QUAD_SCORE = 12;

    private HandlePairs(Deck deck){
        this.deck = deck;
    }

    public static HandlePairs getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandlePairs(deck);
        }
        return singletonInstance;
    }

    @Override
    public int scoreForPlay(Hand cardSet, int playerScore, int playerNum) {
        ArrayList<Card> handCards = cardSet.getCardList();
        Hand cardsToSearch = new Hand(deck);

        if(handCards.size() >= 2) {
            cardsToSearch.insert(handCards.get(handCards.size() - 1), false);
            cardsToSearch.insert(handCards.get(handCards.size() - 2), false);
            playerScore = scorePairs(cardsToSearch, false, playerScore, playerNum);
        }
        if(handCards.size() >= 3) {
            cardsToSearch.insert(handCards.get(handCards.size() - 3), false);
            playerScore = scoreTrips(cardsToSearch, false, playerScore, playerNum);
        }
        if(handCards.size() >= 4) {
            cardsToSearch.insert(handCards.get(handCards.size() - 4), false);
            playerScore = scoreQuads(cardsToSearch, false, playerScore, playerNum);
        }

        return playerScore;
    }

    @Override
    public int scoreForShow(Hand cardSet, int playerScore, int playerNum) {
        playerScore = scorePairs(cardSet, true, playerScore, playerNum);
        playerScore = scoreTrips(cardSet, true, playerScore, playerNum);
        playerScore = scoreQuads(cardSet, true, playerScore, playerNum);
        return playerScore;
    }

    private int scorePairs(Hand cardsToSearch, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractPairs()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + PAIR_SCORE, PAIR_SCORE, null, "pair2", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + PAIR_SCORE, PAIR_SCORE, null, "pair2", null));
            playerScore += PAIR_SCORE;
        }
        return playerScore;
    }

    private int scoreTrips(Hand cardsToSearch, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractTrips()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + TRIP_SCORE, TRIP_SCORE, null, "pair3", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + TRIP_SCORE, TRIP_SCORE, null, "pair3", null));
            playerScore += TRIP_SCORE;
        }
        return playerScore;
    }

    private int scoreQuads(Hand cardsToSearch, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractQuads()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + QUAD_SCORE, QUAD_SCORE, null, "pair4", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + QUAD_SCORE, QUAD_SCORE, null, "pair4", null));
            playerScore += QUAD_SCORE;
        }
        return playerScore;
    }


}
