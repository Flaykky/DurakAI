package game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    protected String name;
    protected List<Card> hand = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addCard(Card card) {
        hand.add(card);
        hand.sort(Card::compareTo);
    }

    public void removeCard(Card card) {
        hand.remove(card);
    }

    public boolean hasCards() {
        return !hand.isEmpty();
    }

    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    // Для переопределения в AI
    public Card chooseAttackCard(Card.Suit trump) {
        return null;
    }

    public Card chooseDefenseCard(Card attackCard, Card.Suit trump) {
        return null;
    }

        // Добавить метод для проверки возможности защиты
        public boolean canDefend(Card attackCard, Card.Suit trump) {
            return hand.stream()
                .anyMatch(c -> isValidDefense(attackCard, c, trump));
        }
    
        private boolean isValidDefense(Card attack, Card defense, Card.Suit trump) {
            return (defense.getSuit() == attack.getSuit() && defense.getValue() > attack.getValue()) ||
                   (defense.getSuit() == trump && attack.getSuit() != trump);
        }
}