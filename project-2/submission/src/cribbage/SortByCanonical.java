package cribbage;

/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

import ch.aplu.jcardgame.Hand;

import java.util.Comparator;

public class SortByCanonical implements Comparator<Hand> {
    @Override
    public int compare(Hand o1, Hand o2) {
        return Cribbage.canonical(o1).compareTo(Cribbage.canonical(o2));
    }
}
