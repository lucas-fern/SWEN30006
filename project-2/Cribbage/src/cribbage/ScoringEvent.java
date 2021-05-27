package cribbage;

import ch.aplu.jcardgame.Hand;

public interface ScoringEvent {
    int scoreForPlay(CribbageEvent event, Hand cardSet, int playerScore, int playerNum);
    int scoreForShow(CribbageEvent event, Hand cardSet, int playerScore, int playerNum);
}
