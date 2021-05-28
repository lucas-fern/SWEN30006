package cribbage;

import ch.aplu.jcardgame.Deck;

public class ScorerFactory {

    public ScoringEvent getScoringEvent(String scorerType, Deck deck){
        if(scorerType.equalsIgnoreCase("TOTAL"))
            return HandleTotals.getInstance(deck);
        else if(scorerType.equalsIgnoreCase("FLUSH"))
            return HandleFlushes.getInstance();

        else if(scorerType.equalsIgnoreCase("PAIR"))
            return HandlePairs.getInstance(deck);

        else if(scorerType.equalsIgnoreCase("RUN"))
            return HandleRuns.getInstance(deck);

        else if(scorerType.equalsIgnoreCase("STARTER"))
            return HandleStarters.getInstance(deck);

        return null;
    }
}
