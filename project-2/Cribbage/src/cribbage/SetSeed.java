package cribbage;

public class SetSeed extends CribbageEvent {
    int seed;

    SetSeed(int seed) {
        super("seed");
        this.seed = seed;
    }
}