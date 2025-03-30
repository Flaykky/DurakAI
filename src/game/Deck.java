package game;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();

    public Deck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 6; value <= 14; value++) {
                cards.push(new Card(value, suit));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card draw() {
        return cards.pop();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card getTrumpCard() {
        return cards.lastElement();
    }

    
    public int size() {
        return cards.size();
    }
}