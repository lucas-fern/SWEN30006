package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

import ch.aplu.jcardgame.Hand;

public class Discard extends CribbageEvent {
    String playerId;
    Hand discards;

    Discard(String playerId, Hand discards) {
        super("discard");
        this.playerId = playerId;
        this.discards = discards;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + Cribbage.canonical(discards);
    }
}