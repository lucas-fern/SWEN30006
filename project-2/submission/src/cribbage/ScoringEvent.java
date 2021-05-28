package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

import ch.aplu.jcardgame.Hand;

public interface ScoringEvent {
    int scoreForPlay(Hand cardSet, int playerScore, int playerNum);
    int scoreForShow(Hand cardSet, int playerScore, int playerNum);
}
