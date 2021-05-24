package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class Show extends CribbageEvent {
    String playerId;
    Card starter;
    Hand hand;

    Show(String playerId, Card starter, Hand hand) {
        super("show");
        this.playerId = playerId;
        this.starter = starter;
        this.hand = hand;
    }
}