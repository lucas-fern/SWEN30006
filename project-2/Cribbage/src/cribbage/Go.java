package cribbage;

public class Go extends CribbageEvent{
    int playerNum;
    Go(int playerNum) {
        super("go");
        this.playerNum = playerNum;
    }
}
