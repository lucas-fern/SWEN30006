package cribbage;

import ch.aplu.jcardgame.Card;

public class Play extends CribbageEvent {
    String playerId;
    int totalPoints;
    int playerNum;
    Card playedCard;

    Play(String playerId, int totalPoints, Card playedCard, int playerNum) {
        super("play");
        this.playerId = playerId;
        this.totalPoints = totalPoints;
        this.playedCard = playedCard;
        this.playerNum = playerNum;
        super.scoringEvent = true;
    }

    @Override
    public String toString() {
        return super.eventId + "," + playerId + "," + totalPoints + "," + Cribbage.canonical(playedCard);
    }
}