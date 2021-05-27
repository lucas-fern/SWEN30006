package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Arrays;



public class CribbageScorer implements CribbageObserver{

    private static ArrayList<ScoringEvent> scorers = new ArrayList<>();

    /**
     * Register a new CribbageScorer.
     * @param newSco the new scorer.
     */
    public static void registerScorer(ScoringEvent newSco) {
        scorers.add(newSco);
    }


    private static Deck deck;
    private static final int LONGEST_RUN = 7;
    private static final int SHORTEST_RUN = 3;
    private static CribbageScorer singletonInstance;
    public static Hand playHistory;
    public static Hand[] playerHands;
    public static int[] playerScores = {0,0};


    private CribbageScorer(Deck deck){
        playHistory = new Hand(deck);
        playerHands = new Hand[2];
        playerHands[0] = new Hand(deck);
        playerHands[1] = new Hand(deck);
        this.deck = deck;

        // Register the scorers to be used for the game
        registerScorer(HandlePairs.getInstance(deck));
        registerScorer(HandleRuns.getInstance(deck));
        registerScorer(HandleTotals.getInstance(deck));
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
            for (ScoringEvent scorer : scorers){
                playerScores[((Play) event).playerNum] = scorer.scoreForPlay(event, playHistory, playerScores[((Play) event).playerNum], ((Play) event).playerNum);
            }
        }
        else if(event.eventId.equals("show")){
            for (ScoringEvent scorer : scorers){
                playerScores[((Show) event).playerNum] = scorer.scoreForShow(event, ((Show) event).hand, playerScores[((Show) event).playerNum], ((Show) event).playerNum);
            }
        }
        else if(event.eventId.equals("go")){
                playerScores[((Go) event).playerNum] += 1;
                Cribbage.notifyObservers(new Score("P" + ((Go) event).playerNum, playerScores[((Go) event).playerNum], 1, null, "go", null));
        }
    }
}
