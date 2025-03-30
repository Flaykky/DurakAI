// src/game/Deck.java
package game;

import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();
    private Card trumpCard;

    public Deck() {
        initializeDeck();
        shuffle();
        // Корректное определение козыря
        if (!cards.isEmpty()) {
            trumpCard = cards.lastElement(); // Козырь - последняя карта в колоде
        }
    }

    private void initializeDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (int value = 6; value <= 14; value++) {
                cards.push(new Card(value, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
        // После перемешивания обновляем козырь
        if (!cards.isEmpty()) {
            trumpCard = cards.lastElement();
        }
    }

    public Card draw() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.pop();
        // Если колода опустела - сброс козыря
        if (cards.isEmpty()) {
            trumpCard = null;
        }
        return card;
    }

    public Card getTrumpCard() {
        return trumpCard;
    }

    public Card.Suit getTrumpSuit() {
        return trumpCard != null ? trumpCard.getSuit() : null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int size() {
        return cards.size();
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }

    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    public boolean isValid() {
        int[] counts = new int[4];
        for (Card card : cards) {
            counts[card.getSuit().ordinal()]++;
        }
        for (int count : counts) {
            if (count != 9) {
                return false;
            }
        }
        return true;
    }

    // Исправлено: добавлен импорт List и удалена лишняя логика
    public void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
        shuffle();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Колода (").append(size()).append(" карт):\n");
        for (Card card : cards) {
            sb.append(card).append(" ");
        }
        return sb.toString();
    }
}
