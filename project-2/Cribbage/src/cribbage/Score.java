package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class Score extends CribbageEvent {
    String playerId;
    int totalScore;
    int thisScore;
    Card starter;
    String scoreType;
    Hand scoreCards;

    Score(String playerId, int totalScore, int thisScore, Card starter, String scoreType, Hand scoreCards) {
        super("score");
        this.playerId = playerId;
        this.totalScore = totalScore;
        this.thisScore = thisScore;
        this.starter = starter;
        this.scoreType = scoreType;
        this.scoreCards = scoreCards;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + totalScore + "," + thisScore + "," + scoreType + "," +
                Cribbage.canonical(scoreCards);
    }
}