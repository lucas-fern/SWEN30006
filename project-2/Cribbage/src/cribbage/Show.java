package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class Show extends CribbageEvent {
    String playerId;
    Card starter;
    int playerNum;
    Hand hand;

    Show(String playerId, Card starter, Hand hand, int playerNum) {
        super("show");
        this.playerId = playerId;
        this.starter = starter;
        this.hand = hand;
        this.playerNum = playerNum;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + Cribbage.canonical(starter) + "+" + Cribbage.canonical(hand);
    }
}