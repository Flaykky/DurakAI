package ai;

import game.Card;
import game.Player;
import game.Game;
import game.Card.Suit;
import java.util.*;
import ai.Logger;

public abstract class AbstractAIPlayer extends Player {
    protected final Random random = new Random();
    protected final Logger logger;

    public AbstractAIPlayer(String name, boolean isHuman) {
        super(name, isHuman);
        this.logger = new Logger(name);
    }

    protected Card chooseRandomCard(List<Card> cards) {
        if (cards.isEmpty()) return null;
        return cards.get(random.nextInt(cards.size()));
    }

    protected Card chooseLowestCard(List<Card> cards, Suit trumpSuit) {
        if (cards.isEmpty()) return null;
        
        cards.sort((c1, c2) -> {
            boolean c1Trump = c1.isTrump(trumpSuit);
            boolean c2Trump = c2.isTrump(trumpSuit);
            
            if (c1Trump && !c2Trump) return 1;
            if (!c1Trump && c2Trump) return -1;
            
            return Integer.compare(c1.getRank().getValue(), c2.getRank().getValue());
        });
        
        return cards.get(0);
    }

    protected Card chooseHighestCard(List<Card> cards, Suit trumpSuit) {
        if (cards.isEmpty()) return null;
        
        cards.sort((c1, c2) -> {
            boolean c1Trump = c1.isTrump(trumpSuit);
            boolean c2Trump = c2.isTrump(trumpSuit);
            
            if (c1Trump && !c2Trump) return -1;
            if (!c1Trump && c2Trump) return 1;
            
            return Integer.compare(c2.getRank().getValue(), c1.getRank().getValue());
        });
        
        return cards.get(0);
    }

    protected List<Card> getTrumpCards(List<Card> cards, Suit trumpSuit) {
        List<Card> trumps = new ArrayList<>();
        for (Card card : cards) {
            if (card.isTrump(trumpSuit)) {
                trumps.add(card);
            }
        }
        return trumps;
    }

    protected List<Card> getNonTrumpCards(List<Card> cards, Suit trumpSuit) {
        List<Card> nonTrumps = new ArrayList<>();
        for (Card card : cards) {
            if (!card.isTrump(trumpSuit)) {
                nonTrumps.add(card);
            }
        }
        return nonTrumps;
    }

    protected boolean hasTrumpCard(Suit trumpSuit) {
        for (Card card : getHand()) {
            if (card.isTrump(trumpSuit)) {
                return true;
            }
        }
        return false;
    }

    protected int countTrumpCards(Suit trumpSuit) {
        int count = 0;
        for (Card card : getHand()) {
            if (card.isTrump(trumpSuit)) {
                count++;
            }
        }
        return count;
    }

    protected Logger getLogger() {
        return logger;
    }
}
