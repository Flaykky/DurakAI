package ai.level3.utils;

import game.Card;
import game.Card.Suit;

import java.util.List;

public class CardPatterns {
    public boolean hasFlush(List<Card> cards) {
        return cards.stream()
                .anyMatch(c -> countSuit(cards, c.getSuit()) >= 3);
    }

    public boolean hasStraight(List<Card> cards) {
        List<Card> sorted = sortCards(cards);
        int sequence = 1;
        
        for (int i = 1; i < sorted.size(); i++) {
            if (sorted.get(i).getValue() == sorted.get(i-1).getValue() + 1) {
                sequence++;
                if (sequence >= 3) return true;
            } else {
                sequence = 1;
            }
        }
        return false;
    }

    private int countSuit(List<Card> cards, Suit suit) {
        return (int) cards.stream()
                .filter(c -> c.getSuit() == suit)
                .count();
    }

    public Card findBestPattern(List<Card> candidates) {
        // Логика выбора лучшей комбинации
        return candidates.get(0);
    }
}
