package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

public abstract class CribbageEvent {
    public final String eventId;
    public boolean scoringEvent = false;

    CribbageEvent(String eventId) {
        this.eventId = eventId;
    }
    public String getPlayerID(){
        return "";
    }
}
