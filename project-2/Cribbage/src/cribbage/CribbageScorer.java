package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Arrays;



public class CribbageScorer implements CribbageObserver{

    private static Deck deck;
    private static final int LONGEST_RUN = 7;
    private static final int SHORTEST_RUN = 3;
    private static CribbageScorer singletonInstance;
    public static Hand playHistory;
    public static Hand[] playerHands;
    public static int totalPlayed = 0;

    private CribbageScorer(Deck deck){
        playHistory = new Hand(deck);
        playerHands = new Hand[2];
        playerHands[0] = new Hand(deck);
        playerHands[1] = new Hand(deck);
        this.deck = deck;
    }

    public static CribbageScorer getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new CribbageScorer(deck);
        }

        return singletonInstance;
    }

    @Override
    public void update(CribbageEvent event) {
        if(event.eventId.equals("starter")){
            playerHands[0].insert(((PlayStarter) event).starter, false);
            playerHands[1].insert(((PlayStarter) event).starter, false);
        }
        // If event is an instance of "Play" add it to the hand history
        else if(event.eventId.equals("play")){
            playHistory.insert(((Play) event).playedCard, true);
            playerHands[((Play) event).playerNum].insert(((Play) event).playedCard, false);
            totalPlayed += Cribbage.cardValue(((Play) event).playedCard);
            determinePlayScore((Play) event, false, playHistory);
        }
        else if(event.eventId.equals("show")){
            System.out.println("in");
            determinePlayScore(event, true, playerHands[((Show) event).playerNum]);
        }
    }

    public int determinePlayScore(CribbageEvent event, boolean displayCards, Hand cardSet){
        return totalAndLastScore(event, cardSet) + runScore(event, displayCards, cardSet) + pairScore(event, cardSet, displayCards);
    }

    private int pairScore(CribbageEvent event, Hand cardSet, boolean displayCards) {

        ArrayList<Card> handCards = cardSet.getCardList();
        Hand cardsToSearch = new Hand(deck);


        if(displayCards){
            scorePairs(cardSet, event, displayCards);
            scoreTrips(cardSet, event, displayCards);
            scoreQuads(cardSet, event, displayCards);
        }
        else{
            if(handCards.size() >= 2) {
                cardsToSearch.insert(handCards.get(handCards.size() - 1), false);
                cardsToSearch.insert(handCards.get(handCards.size() - 2), false);
                scorePairs(cardsToSearch, event, displayCards);
            }
            if(handCards.size() >= 3) {
                cardsToSearch.insert(handCards.get(handCards.size() - 3), false);
                scoreTrips(cardsToSearch, event, displayCards);
            }
            if(handCards.size() >= 4) {
                cardsToSearch.insert(handCards.get(handCards.size() - 4), false);
                scoreQuads(cardsToSearch, event, displayCards);
            }

        }

        return 0;
    }

    private int scorePairs(Hand cardsToSearch, CribbageEvent event, boolean displayCards){
        for (Hand hand : cardsToSearch.extractPairs()){
            if (displayCards)
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 2, null, "pair2", hand));
            else
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 2, null, "pair2", null));

        }
        return 0;
    }

    private int scoreTrips(Hand cardsToSearch, CribbageEvent event, boolean displayCards){
        for (Hand hand : cardsToSearch.extractTrips()){
            if (displayCards)
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 6, null, "pair3", hand));
            else
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 6, null, "pair3", null));
        }
        return 0;
    }

    private int scoreQuads(Hand cardsToSearch, CribbageEvent event, boolean displayCards){
        for (Hand hand : cardsToSearch.extractQuads()){
            if (displayCards)
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 12, null, "pair4", hand));
            else
                Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 12, null, "pair4", null));
        }
        return 0;
    }

    private int runScore(CribbageEvent event, boolean displayCards, Hand cardSet) {

        ArrayList<Card> handCards = cardSet.getCardList();
        int score_from_runs = 0, i = SHORTEST_RUN;

        while(i<=LONGEST_RUN){
            score_from_runs += run_of_size(handCards, cardSet.extractSequences(i), i, event, displayCards);
            i+= 1;
        }

        return score_from_runs;
    }

    private int run_of_size(ArrayList<Card> handCards, Hand[] runs, int runLength, CribbageEvent event, boolean displayCards){
        for (Hand run : runs){
            ArrayList<Card[]> array_run = run.getSequences(runLength);

            if(new ArrayList<>(Arrays.asList(array_run.get(0))).equals(handCards.subList(handCards.size()-runLength, handCards.size()))){
                System.out.println("never");
                if (!displayCards)
                    Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, runLength, null, "run" + runLength, null));
                else
                    Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, runLength, null, "run" + runLength, run));
                return runLength;
            }
        }
        return 0;
    }


    public int totalAndLastScore(CribbageEvent event, Hand cardSet){
        if (totalPlayed == 15 || totalPlayed == 31){
            Cribbage.notifyObservers(new Score(event.getPlayerID(), 0, 2, null, String.valueOf(totalPlayed), cardSet));
            return 2;
        }
        return 0;
    }
}
