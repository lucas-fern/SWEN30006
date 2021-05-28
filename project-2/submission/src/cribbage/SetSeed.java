package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

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