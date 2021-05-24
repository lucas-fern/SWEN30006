package cribbage;

import ch.aplu.jcardgame.Hand;

public class Discard extends CribbageEvent {
    String playerId;
    Hand discards;

    Discard(String playerId, Hand discards) {
        super("discard");
        this.playerId = playerId;
        this.discards = discards;
    }
}