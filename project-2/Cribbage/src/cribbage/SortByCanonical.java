package cribbage;

import ch.aplu.jcardgame.Hand;

import java.util.Comparator;

public class SortByCanonical implements Comparator<Hand> {
    @Override
    public int compare(Hand o1, Hand o2) {
        return Cribbage.canonical(o1).compareTo(Cribbage.canonical(o2));
    }
}
