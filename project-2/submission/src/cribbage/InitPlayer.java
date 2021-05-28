package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

public class InitPlayer extends CribbageEvent {
    String playerId;

    InitPlayer(String playerType, String playerId) {
        super(playerType);
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId;
    }
}