package cribbage;

// Cribbage.java
/* Modified by Workshop 9, Team 2 for SWEN30006 Project 2 */

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cribbage extends CardGame {
    static Cribbage cribbage;  // Provide access to singleton

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public enum Rank {
        // Order of cards is tied to card images
        ACE(1, 1), KING(13, 10), QUEEN(12, 10), JACK(11, 10), TEN(10, 10), NINE(9, 9), EIGHT(8, 8), SEVEN(7, 7), SIX(6, 6), FIVE(5, 5), FOUR(4, 4), THREE(3, 3), TWO(2, 2);
        public final int order;
        public final int value;

        Rank(int order, int value) {
            this.order = order;
            this.value = value;
        }
    }

    static int cardValue(Card c) {
        return ((Cribbage.Rank) c.getRank()).value;
    }

    public Hand cloneHand(Hand h){
        Hand hand = new Hand(deck);
        for (Card card : h.getCardList())
            hand.insert(card.clone(), false);
        return hand;
    }

    /*
    Canonical String representations of Suit, Rank, Card, and Hand
    */
    // TODO: report on the fact that I made all these canonical methods static so they could be used outside
    // (involved making Cribbage.deck static)
    // TODO: potentially extract this behaviour to a new CardFormatHandler class?
    static String canonical(Suit s) {
        return s.toString().substring(0, 1);
    }

    static String canonical(Rank r) {
        switch (r) {
            case ACE:
            case KING:
            case QUEEN:
            case JACK:
            case TEN:
                return r.toString().substring(0, 1);
            default:
                return String.valueOf(r.value);
        }
    }

    static String canonical(Card c) {
        return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit());
    }

    static String canonical(Hand h) {
        Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
        for (Card C : h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
        h1.sort(Hand.SortType.POINTPRIORITY, false);
        return "[" + h1.getCardList().stream().map(Cribbage::canonical).collect(Collectors.joining(",")) + "]";
    }

    static class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
        public int[] values(Enum suit) {  // Returns the value for each card in the suit
            return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order - 1) * (Suit.values().length) + suit.ordinal()).toArray();
        }
    }

    static Random random;

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    static boolean ANIMATE;

    void transfer(Card c, Hand h) {
        if (ANIMATE) {
            c.transfer(h, true);
        } else {
            c.removeFromHand(true);
            h.insert(c, true);
        }
    }

    private void dealingOut(Hand pack, Hand[] hands) {
        for (int i = 0; i < nStartCards; i++) {
            for (int j = 0; j < nPlayers; j++) {
                Card dealt = randomCard(pack);
                dealt.setVerso(false);  // Show the face
                transfer(dealt, hands[j]);
            }
        }
    }

    static int SEED;

    public static Card randomCard(Hand hand) {
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    private final String version = "0.1";
    static public final int nPlayers = 2;
    public final int nStartCards = 6;
    public final int nDiscards = 2;
    private final int handWidth = 400;
    private final int cribWidth = 150;
    private final int segmentWidth = 180;
    private static final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());
    private final Location[] handLocations = {
            new Location(360, 75),
            new Location(360, 625)
    };
    private final Location[] scoreLocations = {
            new Location(590, 25),
            new Location(590, 675)
    };
    private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
            new Location(150, 350),
            new Location(400, 350),
            new Location(650, 350)
    };
    private final Location starterLocation = new Location(50, 625);
    private final Location cribLocation = new Location(700, 625);
    private final Location seedLocation = new Location(5, 25);
    // private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
    private final Actor[] scoreActors = {null, null}; //, null, null };
    private final Location textLocation = new Location(350, 450);
    private final Hand[] hands = new Hand[nPlayers];
    private Hand starter;
    private Hand crib;

    public static void setStatus(String string) {
        cribbage.setStatusText(string);
    }

    static private final IPlayer[] players = new IPlayer[nPlayers];
    //TODO changed from final in report
    private int[] scores = new int[nPlayers];

    final Font normalFont = new Font("Serif", Font.BOLD, 24);
    final Font bigFont = new Font("Serif", Font.BOLD, 36);

    private void initScore() {
        for (int i = 0; i < nPlayers; i++) {
            scores[i] = 0;
            scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
            addActor(scoreActors[i], scoreLocations[i]);
        }
    }

    private void updateScore(int player) {
        removeActor(scoreActors[player]);
        scoreActors[player] = new TextActor(String.valueOf(scores[player]), Color.WHITE, bgColor, bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    /**
     * Deal cards to the players and assign them a player number.
     * @param pack The full deck of cards
     * @param hands The hands of the players to populate
     */
    private void deal(Hand pack, Hand[] hands) {
        for (int i = 0; i < nPlayers; i++) {
            hands[i] = new Hand(deck);
            // players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
            players[i].setId(i);
            players[i].startSegment(deck, hands[i]);

            // Notify observers that player numbers have been assigned.
            notifyObservers(new InitPlayer(players[i].getClass().getName(), players[i].toString()));
        }

        RowLayout[] layouts = new RowLayout[nPlayers];
        for (int i = 0; i < nPlayers; i++) {
            layouts[i] = new RowLayout(handLocations[i], handWidth);
            layouts[i].setRotationAngle(0);
            // layouts[i].setStepDelay(10);
            hands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
        layouts[0].setStepDelay(0);

        dealingOut(pack, hands);
        for (int i = 0; i < nPlayers; i++) {
            hands[i].sort(Hand.SortType.POINTPRIORITY, true);

            // Notify observers that the deal has been performed.
            notifyObservers(new Deal(players[i].toString(), hands[i]));
        }
        layouts[0].setStepDelay(0);
    }

    private void discardToCrib() {
        crib = new Hand(deck);
        RowLayout layout = new RowLayout(cribLocation, cribWidth);
        layout.setRotationAngle(0);
        crib.setView(this, layout);
        // crib.setTargetArea(cribTarget);
        crib.draw();
        for (IPlayer player : players) {
            Hand playerDiscards = new Hand(deck);
            for (int i = 0; i < nDiscards; i++) {
                Card discard = player.discard();
                transfer(discard, crib);
                playerDiscards.insert(discard.clone(), false);
            }
            // Notify observers of the discarded cards
            playerDiscards.sort(Hand.SortType.POINTPRIORITY, false);
            notifyObservers(new Discard(player.toString(), playerDiscards));

            crib.sort(Hand.SortType.POINTPRIORITY, true);
        }
    }

    private void starter(Hand pack) {
        starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        starter.setView(this, layout);
        starter.draw();
        Card dealt = randomCard(pack);
        dealt.setVerso(false);
        transfer(dealt, starter);

        // notify observers that the starter card has been picked
        notifyObservers(new PlayStarter(dealt));
    }

    int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += cardValue(c);
        return total;
    }

    class Segment {
        Hand segment;
        boolean go;
        int lastPlayer;
        boolean newSegment;

        void reset(final List<Hand> segments) {
            segment = new Hand(deck);
            segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
            segment.draw();
            go = false;        // No-one has said "go" yet
            lastPlayer = -1;   // No-one has played a card yet in this segment
            newSegment = false;  // Not ready for new segment yet
        }
    }

    private void play() {
        final int thirtyone = 31;
        List<Hand> segments = new ArrayList<>();
        int currentPlayer = 0; // Player 1 is dealer
        Segment s = new Segment();
        s.reset(segments);
        while (!(players[0].emptyHand() && players[1].emptyHand())) {
            // System.out.println("segments.size() = " + segments.size());
            Card nextCard = players[currentPlayer].lay(thirtyone - total(s.segment));
            if (nextCard == null) {
                if (s.go) {
                    // Another "go" after previous one with no intervening cards
                    // lastPlayer gets 1 point for a "go"
                    CribbageScorer.getInstance(deck).scoreNoPlay(s.lastPlayer);
                    s.newSegment = true;
                } else {
                    // currentPlayer says "go"
                    s.go = true;
                }
                currentPlayer = (currentPlayer + 1) % 2;
            } else {
                s.lastPlayer = currentPlayer; // last Player to play a card in this segment
                transfer(nextCard, s.segment);

                // Notify observers that a card has been played into the segment
                notifyObservers(new Play(players[currentPlayer].toString(), total(s.segment), nextCard, currentPlayer));

                if (total(s.segment) == thirtyone) {
                    // lastPlayer gets 2 points for a 31
                    s.newSegment = true;
                    currentPlayer = (currentPlayer + 1) % 2;
                } else {
                    // if total(segment) == 15, lastPlayer gets 2 points for a 15
                    if (!s.go) { // if it is "go" then same player gets another turn
                        currentPlayer = (currentPlayer + 1) % 2;
                    }
                }
            }
            if (s.newSegment) {
                CribbageScorer.playHistory.removeAll(false);
                segments.add(s.segment);
                s.reset(segments);
            }
            scores = CribbageScorer.playerScores;
            updateScore(0);
            updateScore(1);
        }
        CribbageScorer.getInstance(deck).scoreNoPlay(s.lastPlayer);
    }

    void showHandsCrib() {
        // TODO:
        // score player 0 (non dealer)
        notifyObservers(new Show(players[0].toString(), starter.getLast(), CribbageScorer.playerHands[0], 0));
        scores = CribbageScorer.playerScores;
        updateScore(0);
        // score player 1 (dealer)
        notifyObservers(new Show(players[1].toString(), starter.getLast(), CribbageScorer.playerHands[1], 1));
        scores = CribbageScorer.playerScores;
        updateScore(1);
        // score crib (for dealer)
        Hand clonedCrib = cloneHand(crib);
        clonedCrib.insert(starter.getLast().clone(), false);
        notifyObservers(new Show(players[1].toString(), starter.getLast(), clonedCrib, 1));
        scores = CribbageScorer.playerScores;
        updateScore(1);
        // notify observers of each show
    }

    private static ArrayList<CribbageObserver> observers = new ArrayList<>();

    /**
     * Register a new CribbageObserver.
     * @param newObs the new observer.
     */
    public static void registerObserver(CribbageObserver newObs) {
        observers.add(newObs);
    }

    /**
     * Notify CribbageObservers of a new CribbageEvent
     * @param event The event which has taken place.
     */
    // TODO: report that this has been changed to public function
    public static void notifyObservers(CribbageEvent event) {
        for (CribbageObserver obs: observers) {
            obs.update(event);
        }
    }

    public Cribbage() {
        super(850, 700, 30);
        cribbage = this;
        setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        initScore();

        Hand pack = deck.toHand(false);
        RowLayout layout = new RowLayout(starterLocation, 0);
        layout.setRotationAngle(0);
        pack.setView(this, layout);
        pack.setVerso(true);
        pack.draw();
        addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);

        /* Play the round */
        deal(pack, hands);
        discardToCrib();
        starter(pack);
        play();
        showHandsCrib();

        addActor(new Actor("sprites/gameover.gif"), textLocation);
        setStatusText("Game over.");
        refresh();
    }

    public static void main(String[] args)
            throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        // Start and register the CribbageLogger
        registerObserver(CribbageLogger.getInstance());
        registerObserver(CribbageScorer.getInstance(deck));

        /* Handle Properties */
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Properties cribbageProperties = new Properties();
        // Default properties
        cribbageProperties.setProperty("Animate", "true");
        cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
        cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

        // Read properties
        try (FileReader inStream = new FileReader("cribbage.properties")) {
            cribbageProperties.load(inStream);
        }

        // Control Graphics
        ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

        // Control Randomisation
        /* Read the first argument and save it as a seed if it exists */
        if (args.length > 0) { // Use arg seed - overrides property
            SEED = Integer.parseInt(args[0]);
        } else { // No arg
            String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
            if (seedProp != null) { // Use property seed
                SEED = Integer.parseInt(seedProp);
            } else { // and no property
                SEED = new Random().nextInt(); // so randomise
            }
        }

        // Notify observers that the seed has been set
        notifyObservers(new SetSeed(SEED));

        random = new Random(SEED);

        // Control Player Types
        // Initialise P0
        Class<?> clazz;
        clazz = Class.forName(cribbageProperties.getProperty("Player0"));
        players[0] = (IPlayer) clazz.getConstructor().newInstance();

        // Initialise P1
        clazz = Class.forName(cribbageProperties.getProperty("Player1"));
        players[1] = (IPlayer) clazz.getConstructor().newInstance();

        // End properties

        new Cribbage();
    }

    public int saleMade() {
        return Counter.getnext();
    }
}



