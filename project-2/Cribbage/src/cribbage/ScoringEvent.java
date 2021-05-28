package cribbage;

import ch.aplu.jcardgame.Hand;

public interface ScoringEvent {
    int scoreForPlay(Hand cardSet, int playerScore, int playerNum);
    int scoreForShow(Hand cardSet, int playerScore, int playerNum);
}
