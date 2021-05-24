package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class Score extends CribbageEvent {
    String playerId;
    int totalScore;
    int thisScore;
    Card starter;
    Hand scoreCards;

    Score(String playerId, int totalScore, int thisScore, Card starter, Hand scoreCards) {
        super("score");
        this.playerId = playerId;
        this.totalScore = totalScore;
        this.thisScore = thisScore;
        this.starter = starter;
        this.scoreCards = scoreCards;
    }
}