package cribbage;

import ch.aplu.jcardgame.Card;

public class Play extends CribbageEvent {
    String playerId;
    int totalPoints;
    Card playedCard;

    Play(String playerId, int totalPoints, Card playedCard) {
        super("play");
        this.playerId = playerId;
        this.totalPoints = totalPoints;
        this.playedCard = playedCard;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + totalPoints + "," + playedCard;
    }
}