package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class HandlePairs implements ScoringEvent{

    private static Deck deck;
    private static HandlePairs singletonInstance;

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
    public int scoreForPlay(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        ArrayList<Card> handCards = cardSet.getCardList();
        Hand cardsToSearch = new Hand(deck);

        if(handCards.size() >= 2) {
            cardsToSearch.insert(handCards.get(handCards.size() - 1), false);
            cardsToSearch.insert(handCards.get(handCards.size() - 2), false);
            playerScore = scorePairs(cardsToSearch, event, false, playerScore, playerNum);
        }
        if(handCards.size() >= 3) {
            cardsToSearch.insert(handCards.get(handCards.size() - 3), false);
            playerScore = scoreTrips(cardsToSearch, event, false, playerScore, playerNum);
        }
        if(handCards.size() >= 4) {
            cardsToSearch.insert(handCards.get(handCards.size() - 4), false);
            playerScore = scoreQuads(cardsToSearch, event, false, playerScore, playerNum);
        }

        return playerScore;
    }

    @Override
    public int scoreForShow(CribbageEvent event, Hand cardSet, int playerScore, int playerNum) {
        playerScore = scorePairs(cardSet, event, true, playerScore, playerNum);
        playerScore = scoreTrips(cardSet, event, true, playerScore, playerNum);
        playerScore = scoreQuads(cardSet, event, true, playerScore, playerNum);
        return playerScore;
    }

    private int scorePairs(Hand cardsToSearch, CribbageEvent event, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractPairs()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 2, 2, null, "pair2", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 2, 2, null, "pair2", null));
            playerScore += 2;
        }
        return playerScore;
    }

    private int scoreTrips(Hand cardsToSearch, CribbageEvent event, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractTrips()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 6, 6, null, "pair3", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 6, 6, null, "pair3", null));
            playerScore += 6;
        }
        return playerScore;
    }

    private int scoreQuads(Hand cardsToSearch, CribbageEvent event, boolean displayCards, int playerScore, int playerNum){
        for (Hand hand : cardsToSearch.extractQuads()){
            if (displayCards)
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 12, 12, null, "pair4", hand));
            else
                Cribbage.notifyObservers(new Score("P" + playerNum, playerScore + 12, 12, null, "pair4", null));
            playerScore += 12;
        }
        return playerScore;
    }


}
