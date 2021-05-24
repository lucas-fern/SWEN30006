package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public abstract class CribbageEvent {
    public final String eventId;

    CribbageEvent(String eventId) {
        this.eventId = eventId;
    }

    public class SetSeed extends CribbageEvent {
        int seed;

        SetSeed(int seed) {
            super("seed");
            this.seed = seed;
        }
    }

    public class InitPlayer extends CribbageEvent {
        String playerId;

        InitPlayer(String playerType, String playerId) {
            super(playerType);
            this.playerId = playerId;
        }
    }

    public class Deal extends CribbageEvent {
        String playerId;
        Hand hand;

        Deal(String playerId, Hand hand) {
            super("deal");
            this.playerId = playerId;
            this.hand = hand;
        }
    }

    public class Discard extends CribbageEvent {
        String playerId;
        Hand discards;

        Discard(String playerId, Hand discards) {
            super("discard");
            this.playerId = playerId;
            this.discards = discards;
        }
    }

    public class PlayStarter extends CribbageEvent {
        Card starter;

        PlayStarter(Card starter) {
            super("starter");
            this.starter = starter;
        }
    }

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
    }

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

    public class Score extends CribbageEvent {
        String playerId;
        int totalScore;
        int thisScore;
        Card starter;
        Hand scoreCards;

        Score(String playerId, int totalScore, int thisScore, Card starter, Hand scoreCards) {
            super("score");
            this.playerId = playerId;
            this.totalScore = totalScore;
            this.thisScore = thisScore;
            this.starter = starter;
            this.scoreCards = scoreCards;
        }
    }
}
