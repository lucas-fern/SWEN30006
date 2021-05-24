package cribbage;

public abstract class CribbageEvent {
    public final String eventId;

    CribbageEvent(String eventId) {
        this.eventId = eventId;
    }
}
