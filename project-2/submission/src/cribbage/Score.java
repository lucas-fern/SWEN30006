package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

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
        if (scoreCards == null)
            return super.eventId + "," + playerId + "," + totalScore + "," + thisScore + "," + scoreType;
        else
            return super.eventId + "," + playerId + "," + totalScore + "," + thisScore + "," + scoreType + "," +
                Cribbage.canonical(scoreCards);
    }
}