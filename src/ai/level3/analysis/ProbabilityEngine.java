package ai.level3.analysis;

import game.Card;
import game.Card.Suit;
import java.util.*;
import java.util.stream.Collectors;

public class ProbabilityEngine {
    private final Set<Card> knownCards = new HashSet<>();
    private final Map<Suit, Integer> remainingSuitCounts = new HashMap<>();
    private final Map<Integer, Integer> remainingValueCounts = new HashMap<>();
    private int totalUnknownCards = 36; // 36 карт в колоде

    public ProbabilityEngine() {
        initializeCounts();
    }

    private void initializeCounts() {
        for (Suit suit : Suit.values()) {
            remainingSuitCounts.put(suit, 9); // 6-14 (9 карт)
        }
        for (int value = 6; value <= 14; value++) {
            remainingValueCounts.put(value, 4); // По 4 карты каждого номинала
        }
    }

    public void update(Card card) {
        knownCards.add(card);
        remainingSuitCounts.put(card.getSuit(), remainingSuitCounts.get(card.getSuit()) - 1);
        remainingValueCounts.put(card.getValue(), remainingValueCounts.get(card.getValue()) - 1);
        totalUnknownCards--;
    }

    public double getProbability(Card card) {
        if (knownCards.contains(card)) return 0.0;
        int suitLeft = remainingSuitCounts.get(card.getSuit());
        int valueLeft = remainingValueCounts.get(card.getValue());
        return (double) (suitLeft * valueLeft) / totalUnknownCards;
    }

    public double getDefenseProbability(Card attackCard, Suit trump) {
        double probability = 0.0;
        
        // Вероятность покрыть картой той же масти
        List<Card> sameSuit = generatePossibleCards(attackCard.getSuit(), 
            v -> v > attackCard.getValue());
        probability += calculateProbability(sameSuit);
        
        // Вероятность покрыть козырем
        if (attackCard.getSuit() != trump) {
            List<Card> trumpCards = generatePossibleCards(trump, 
                v -> true);
            probability += calculateProbability(trumpCards);
        }
        
        return probability;
    }

    public double getAttackProbability(Card attackCard, Suit trump) {
        double probability = 1.0;
        
        // Вероятность, что у противника нет защиты
        probability -= getDefenseProbability(attackCard, trump);
        
        // Вероятность подкидывания
        int sameValueCount = remainingValueCounts.get(attackCard.getValue());
        probability *= (sameValueCount / (double) totalUnknownCards);
        
        return probability;
    }

    private List<Card> generatePossibleCards(Suit suit, java.util.function.Predicate<Integer> valueFilter) {
        List<Card> possible = new ArrayList<>();
        for (int value = 6; value <= 14; value++) {
            if (valueFilter.test(value)) {
                Card card = new Card(value, suit);
                if (!knownCards.contains(card)) {
                    possible.add(card);
                }
            }
        }
        return possible;
    }

    private double calculateProbability(List<Card> possibleCards) {
        int totalPossible = possibleCards.size();
        if (totalPossible == 0) return 0.0;
        
        double singleCardProb = 1.0 / totalUnknownCards;
        return singleCardProb * totalPossible;
    }

    public Map<Card, Double> getCardProbabilities(Suit suit) {
        Map<Card, Double> probabilities = new HashMap<>();
        for (int value = 6; value <= 14; value++) {
            Card card = new Card(value, suit);
            if (!knownCards.contains(card)) {
                double prob = (remainingSuitCounts.get(suit) * 
                              remainingValueCounts.get(value)) / 
                              (double) totalUnknownCards;
                probabilities.put(card, prob);
            }
        }
        return probabilities;
    }

    public double getTrumpThreatLevel(Suit trump) {
        int remainingTrumps = remainingSuitCounts.get(trump);
        return (double) remainingTrumps / totalUnknownCards;
    }

    public void reset() {
        knownCards.clear();
        initializeCounts();
        totalUnknownCards = 36;
    }
}
