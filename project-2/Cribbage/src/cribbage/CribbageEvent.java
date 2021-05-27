package cribbage;

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
