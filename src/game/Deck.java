// src/game/Deck.java
package game;

import java.util.Collections;
import java.util.Stack;

public class Deck {
    private final Stack<Card> cards = new Stack<>();
    private Card trumpCard;

    public Deck() {
        initializeDeck();
        shuffle();
        setTrumpCard();
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
        // После перемешивания обновляем козырную карту
        if (!cards.isEmpty()) {
            trumpCard = cards.lastElement();
        }
    }

    public Card draw() {
        if (cards.isEmpty()) {
            return null;
        }
        Card card = cards.pop();
        // Если после взятия карты колода пуста, сбрасываем козырь
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

    // Проверка наличия конкретной карты в колоде
    public boolean contains(Card card) {
        return cards.contains(card);
    }

    // Получение карты по индексу (для отладки)
    public Card getCard(int index) {
        if (index >= 0 && index < cards.size()) {
            return cards.get(index);
        }
        return null;
    }

    // Метод для проверки корректности колоды
    public boolean isValid() {
        int[] counts = new int[4]; // Для подсчета мастей
        for (Card card : cards) {
            counts[card.getSuit().ordinal()]++;
        }
        // В колоде должно быть по 9 карт каждой масти (6-14)
        for (int count : counts) {
            if (count != 9) {
                return false;
            }
        }
        return true;
    }

    // Восстановление колоды из отбоя (для будущих версий)
    public void addCards(List<Card> cardsToAdd) {
        cards.addAll(cardsToAdd);
        shuffle();
    }

    // Отладочный вывод колоды
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
