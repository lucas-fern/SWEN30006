package cribbage;

import ch.aplu.jcardgame.Card;

public class PlayStarter extends CribbageEvent {
    Card starter;

    PlayStarter(Card starter) {
        super("starter");
        this.starter = starter;
    }

    @Override
    public String toString() {
        return super.eventId + "," + Cribbage.canonical(starter);
    }
}