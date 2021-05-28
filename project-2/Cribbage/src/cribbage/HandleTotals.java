package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class HandleTotals implements ScoringEvent{

    private final Deck deck;
    private static HandleTotals singletonInstance;
    private static CribbageLogger logger;
    private static final int POINTS_FOR_TOTALS = 2;


    private HandleTotals(Deck deck){
        logger = CribbageLogger.getInstance();
        this.deck = deck;
    }

    public static HandleTotals getInstance(Deck deck) {
        if (singletonInstance == null) {
            singletonInstance = new HandleTotals(deck);
        }
        return singletonInstance;
    }


    @Override
    public int scoreForPlay(Hand cardSet, int playerScore, int playerNum) {
        int totalPlayed = 0;
        for (Card card : cardSet.getCardList()) totalPlayed += Cribbage.cardValue(card);

        if(totalPlayed == 15 || totalPlayed == 31) {
            String number;
            if (totalPlayed == 15)
                number = "fifteen";
            else
                number = "thirtyone";
            logger.log(new Score("P" + playerNum, playerScore + POINTS_FOR_TOTALS, POINTS_FOR_TOTALS, null, number, null));
            playerScore += POINTS_FOR_TOTALS;
        }
        return playerScore;
    }

    @Override
    public int scoreForShow(Hand cardSet, int playerScore, int playerNum) {
        ArrayList<Hand> pairs = new ArrayList<>();
        ArrayList<Hand> triples = new ArrayList<>();
        ArrayList<Hand> quads = new ArrayList<>();

        ArrayList<int[]> usedPairs = new ArrayList<>();

        for (Card first_card : cardSet.getCardList()){
            for (Card second_card : cardSet.getCardList()){
                if(checkValidPair(first_card, second_card, usedPairs)) {
                    int[] numberPair = {first_card.getCardNumber(), second_card.getCardNumber()};
                    usedPairs.add(numberPair);
                    Hand _a = new Hand(deck);
                    Hand _b = Cribbage.cribbage.cloneHand(cardSet);

                    _a.insert(_b.getCard(first_card.getCardNumber()).clone(), false);
                    _a.insert(_b.getCard(second_card.getCardNumber()).clone(), false);
                    _b.remove(_b.getCard(first_card.getCardNumber()), false);
                    _b.remove(_b.getCard(second_card.getCardNumber()), false);

                    pairs.add(_a);
                    triples.add(_b);
                }
            }
        }

        for (Card card : cardSet.getCardList()){
            Hand _c = Cribbage.cribbage.cloneHand(cardSet);
            _c.remove(card.clone(), false);
            quads.add(_c);
        }

        ArrayList<Hand> allHands = new ArrayList<>(pairs);
        for(int i = triples.size()-1; i >=0; i--) allHands.add(triples.get(i));
        allHands.addAll(quads);
        allHands.add(cardSet);

        for (Hand hand : allHands){
            int score = 0;
            for (Card card : hand.getCardList()) score += Cribbage.cardValue(card);

            if (score == 15) {
                logger.log(new Score("P" + playerNum, playerScore + POINTS_FOR_TOTALS, POINTS_FOR_TOTALS, null, "fifteen", hand));
                playerScore += POINTS_FOR_TOTALS;
            }
        }

        return playerScore;


    }

    private boolean checkValidPair(Card a, Card b, ArrayList<int[]> usedPairs){
        if(a.getCardNumber() == b.getCardNumber()) return false;
        for (int[] pair : usedPairs) {
            if (pair[0] == a.getCardNumber() && pair[1] == b.getCardNumber()) return false;
            if (pair[0] == b.getCardNumber() && pair[1] == a.getCardNumber()) return false;
        }

        return true;

    }
}
