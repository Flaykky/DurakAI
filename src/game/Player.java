// src/game/Player.java
package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    protected String name;
    protected List<Card> hand = new ArrayList<>();
    private int totalCardsTaken = 0; // Статистика

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        if (card != null) {
            hand.add(card);
            sortHand();
        }
    }

    public void addCards(List<Card> cards) {
        hand.addAll(cards);
        sortHand();
    }

    public void removeCard(Card card) {
        if (hand.remove(card)) {
            totalCardsTaken++; // Статистика
        }
    }

    public boolean hasCards() {
        return !hand.isEmpty();
    }

    public List<Card> getHand() {
        return new ArrayList<>(hand); // Возвращаем копию для безопасности
    }

    public String getName() {
        return name;
    }

    protected void sortHand() {
        Collections.sort(hand);
    }

    // Поиск карт по различным критериям
    public List<Card> getCardsBySuit(Card.Suit suit) {
        List<Card> result = new ArrayList<>();
        for (Card card : hand) {
            if (card.getSuit() == suit) {
                result.add(card);
            }
        }
        return result;
    }

    public List<Card> getTrumpCards(Card.Suit trump) {
        return getCardsBySuit(trump);
    }

    public List<Card> getCardsByValue(int value) {
        List<Card> result = new ArrayList<>();
        for (Card card : hand) {
            if (card.getValue() == value) {
                result.add(card);
            }
        }
        return result;
    }

    // Проверка возможностей
    public boolean canAttack(Card.Suit trump) {
        return !hand.isEmpty();
    }

    public boolean canDefend(Card attackCard, Card.Suit trump) {
        for (Card card : hand) {
            if (card.canBeat(attackCard, trump)) {
                return true;
            }
        }
        return false;
    }

    public boolean canAddToAttack(List<Card> attackCards) {
        if (attackCards.isEmpty()) return false;
        
        int attackValue = attackCards.get(0).getValue();
        for (Card card : hand) {
            if (card.getValue() == attackValue) {
                return true;
            }
        }
        return false;
    }

    // Статистика и информация
    public int getHandSize() {
        return hand.size();
    }

    public int getTotalCardsTaken() {
        return totalCardsTaken;
    }

    public Card getWeakestCard() {
        if (hand.isEmpty()) return null;
        return hand.get(0);
    }

    public Card getStrongestCard() {
        if (hand.isEmpty()) return null;
        return hand.get(hand.size()-1);
    }

    // Для отладки
    public String handToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hand.size(); i++) {
            sb.append(String.format("%2d: %s\n", i, hand.get(i).toFullString()));
        }
        return sb.toString();
    }

    // Заглушки для AI (будут реализованы в наследниках)
    public Card chooseAttackCard(Card.Suit trump) {
        return null;
    }

    public Card chooseDefenseCard(Card attackCard, Card.Suit trump) {
        return null;
    }
}
