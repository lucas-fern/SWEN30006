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
        registerScorer(HandleTotals.getInstance(deck));
        registerScorer(HandleFlushes.getInstance());
        registerScorer(HandlePairs.getInstance(deck));
        registerScorer(HandleRuns.getInstance(deck));
        registerScorer(HandleStarters.getInstance(deck));
    }

    public static CribbageScorer getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new CribbageScorer(deck);
        }

        return singletonInstance;
    }

    @Override
    public void update(CribbageEvent event) {
        // If event is an instance of "Play" add it to the hand history
        switch (event.eventId) {
            case "starter" -> {
                playerHands[0].insert(((PlayStarter) event).starter.clone(), false);
                playerHands[1].insert(((PlayStarter) event).starter.clone(), false);
                notifyScorers(1, playerHands[0], false);
            }
            case "play" -> {
                playHistory.insert(((Play) event).playedCard.clone(), false);
                playerHands[((Play) event).playerNum].insert(((Play) event).playedCard.clone(), false);
                notifyScorers(((Play) event).playerNum, playHistory, false);
            }
            case "show" -> notifyScorers(((Show) event).playerNum, ((Show) event).hand, true);
        }
    }

    public void scoreNoPlay(int playerNum){
        playerScores[playerNum] += 1;
        playHistory.removeAll(false);
        CribbageLogger.getInstance().log(new Score("P" + playerNum, playerScores[playerNum], 1, null, "go", null));
    }
}
