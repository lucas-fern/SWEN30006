package cribbage;

public class SetSeed extends CribbageEvent {
    int seed;

    SetSeed(int seed) {
        super("seed");
        this.seed = seed;
    }

    @Override
    public String toString() {
        return super.eventId + "," + seed;
    }
}