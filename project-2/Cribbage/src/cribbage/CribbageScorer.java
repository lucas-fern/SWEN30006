package cribbage;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;



public class CribbageScorer implements CribbageObserver{

    private static final ArrayList<ScoringEvent> scorers = new ArrayList<>();

    /**
     * Register a new CribbageScorer.
     * @param newSco the new scorer.
     */
    public static void registerScorer(ScoringEvent newSco) {
        scorers.add(newSco);
    }

    public static void notifyScorers(int playerNum, Hand cardSet, boolean showScore){
        for (ScoringEvent scorer : scorers) {
            if (!showScore) playerScores[playerNum] = scorer.scoreForPlay(cardSet, playerScores[playerNum], playerNum);
            else playerScores[playerNum] = scorer.scoreForShow(cardSet, playerScores[playerNum], playerNum);
        }
    }

    private static CribbageScorer singletonInstance;
    public static Hand playHistory;
    public static Hand[] playerHands;
    public static int[] playerScores = {0,0};


    private CribbageScorer(Deck deck){
        playHistory = new Hand(deck);
        playerHands = new Hand[]{new Hand(deck), new Hand(deck)};

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
        switch (event.eventId) {
            case "starter":
                playerHands[0].insert(((PlayStarter) event).starter, false);
                playerHands[1].insert(((PlayStarter) event).starter, false);
                break;
            // If event is an instance of "Play" add it to the hand history
            case "play":
                playHistory.insert(((Play) event).playedCard, false);
                playerHands[((Play) event).playerNum].insert(((Play) event).playedCard, false);
                notifyScorers(((Play) event).playerNum, playHistory, false);
                break;
            case "show":
                notifyScorers(((Show) event).playerNum, ((Show) event).hand, true);
                break;
            case "go":
                playerScores[((Go) event).playerNum] += 1;
                Cribbage.notifyObservers(new Score("P" + ((Go) event).playerNum, playerScores[((Go) event).playerNum], 1, null, "go", null));
                break;
        }
    }
}
