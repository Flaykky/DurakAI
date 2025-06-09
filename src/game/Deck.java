package game;

import java.util.*;
import game.Card.Suit;

public class Deck {
    private final List<Card> cards = new ArrayList<>(36);
    private final Random random = new Random();
    private Suit trumpSuit;
    
    public Deck() {
        initializeDeck();
        shuffle();
        if (!cards.isEmpty()) {
            trumpSuit = cards.get(0).getSuit();
        }
    }
    
    private void initializeDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }
    
    public void shuffle() {
        for (int i = 0; i < cards.size(); i++) {
            int randomIndex = random.nextInt(cards.size());
            Collections.swap(cards, i, randomIndex);
        }
    }
    
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }
    
    public int remainingCards() {
        return cards.size();
    }
    
    public Suit getTrumpSuit() {
        return trumpSuit;
    }
    
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    public void revealTrump() {
        if (!cards.isEmpty()) {
            cards.get(0).setFaceUp(true);
        }
    }
    
    public void returnCards(List<Card> cardsToReturn) {
        cards.addAll(cardsToReturn);
        shuffle();
    }
    
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    public int getSize() {
        return cards.size();
    }
    
    public void removeCard(Card card) {
        cards.remove(card);
    }
    
    public void removeCards(List<Card> cardsToRemove) {
        cards.removeAll(cardsToRemove);
    }
    
    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }
}