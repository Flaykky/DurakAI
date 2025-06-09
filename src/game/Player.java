package game;

import java.util.*;
import game.Card.Suit;
import game.Card.Rank;

public abstract class Player {
    private final String name;
    protected final List<Card> hand = new ArrayList<>();
    protected final boolean isHuman;
    
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
    }
    
    public String getName() {
        return name;
    }
    
    public List<Card> getHand() {
        return new ArrayList<>(hand);
    }
    
    public int getHandSize() {
        return hand.size();
    }
    
    public boolean isHuman() {
        return isHuman;
    }
    
    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }
    
    public void addCards(List<Card> cards) {
        for (Card card : cards) {
            if (card != null) {
                hand.add(card);
            }
        }
    }
    
    public void removeCard(Card card) {
        hand.remove(card);
    }
    
    public void sortHand(Suit trumpSuit) {
        hand.sort((c1, c2) -> {
            // Сначала по козырности
            boolean c1Trump = c1.isTrump(trumpSuit);
            boolean c2Trump = c2.isTrump(trumpSuit);
            
            if (c1Trump && !c2Trump) return -1;
            if (!c1Trump && c2Trump) return 1;
            
            // Затем по масти
            int suitComparison = c1.getSuit().compareTo(c2.getSuit());
            if (suitComparison != 0) {
                return suitComparison;
            }
            
            // Затем по значению
            return c1.getRank().compareTo(c2.getRank());
        });
    }
    
    public abstract Card playCard(Game game, List<Card> cardsOnTable);
    
    public abstract Card defendCard(Game game, Card attackCard);
    
    public boolean hasCards() {
        return !hand.isEmpty();
    }
    
    public void clearHand() {
        hand.clear();
    }
    
    public boolean containsCard(Card card) {
        return hand.contains(card);
    }
    
    public void showAllCards() {
        for (Card card : hand) {
            card.setFaceUp(true);
        }
    }
    
    public boolean canPlayCard(Card card, Suit trumpSuit) {
        return hand.contains(card);
    }
    
    public boolean canDefend(Card attackCard, Card defendCard, Suit trumpSuit) {
        if (defendCard == null) return false;
        return defendCard.beats(attackCard, trumpSuit);
    }
    
    public boolean hasBeatableCard(Card attackCard, Suit trumpSuit) {
        for (Card card : hand) {
            if (card.beats(attackCard, trumpSuit)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean canAttackWithCard(Card card, List<Card> cardsOnTable) {
        if (cardsOnTable.isEmpty()) {
            return true;
        }
        
        for (Card tableCard : cardsOnTable) {
            if (tableCard.getRank() == card.getRank()) {
                return true;
            }
        }
        return false;
    }
    
    public List<Card> getPlayableCards(List<Card> cardsOnTable) {
        List<Card> playable = new ArrayList<>();
        
        if (cardsOnTable.isEmpty()) {
            playable.addAll(hand);
        } else {
            for (Card card : hand) {
                for (Card tableCard : cardsOnTable) {
                    if (card.getRank() == tableCard.getRank()) {
                        playable.add(card);
                        break;
                    }
                }
            }
        }
        
        return playable;
    }
    
    public List<Card> getBeatableCards(Card attackCard, Suit trumpSuit) {
        List<Card> beatable = new ArrayList<>();
        for (Card card : hand) {
            if (card.beats(attackCard, trumpSuit)) {
                beatable.add(card);
            }
        }
        return beatable;
    }
}