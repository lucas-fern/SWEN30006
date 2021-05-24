package cribbage;

public class InitPlayer extends CribbageEvent {
    String playerId;

    InitPlayer(String playerType, String playerId) {
        super(playerType);
        this.playerId = playerId;
    }
}