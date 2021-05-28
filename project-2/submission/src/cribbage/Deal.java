package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

import ch.aplu.jcardgame.Hand;

public class Deal extends CribbageEvent {
    String playerId;
    Hand hand;

    Deal(String playerId, Hand hand) {
        super("deal");
        this.playerId = playerId;
        this.hand = hand;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + Cribbage.canonical(hand);
    }
}