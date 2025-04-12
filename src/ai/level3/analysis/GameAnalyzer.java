// src/ai/level3/analysis/GameAnalyzer.java
package ai.level3.analysis;

import game.Card;
import game.Card.Suit;
import ai.level3.memory.CardMemory;
import ai.level3.analysis.ProbabilityEngine;
import java.util.*;
import java.util.stream.Collectors;

public class GameAnalyzer {
    private final CardMemory memory;
    private final ProbabilityEngine probabilityEngine;
    private final GameState currentState;

    public GameAnalyzer(CardMemory memory, ProbabilityEngine probabilityEngine) {
        this.memory = memory;
        this.probabilityEngine = probabilityEngine;
        this.currentState = new GameState();
    }

    // Основные методы анализа
    public double evaluateHandStrength(List<Card> hand, Suit trump) {
        return hand.stream()
                .mapToDouble(card -> calculateCardStrength(card, trump))
                .sum();
    }

    private double calculateCardStrength(Card card, Suit trump) {
        double base = card.getValue() * 1.5;
        if (card.getSuit() == trump) base *= 2;
        return base * probabilityEngine.getProbability(card);
    }

    public Card getMostDangerousCard(List<Card> attackCards, Suit trump) {
        return attackCards.stream()
                .max(Comparator.comparingDouble(
                        card -> card.getValue() * (card.isTrump(trump) ? 2 : 1)
                ))
                .orElse(null);
    }

    public boolean isTrumpSafe(Suit trump) {
        return probabilityEngine.getTrumpThreatLevel(trump) < 0.3;
    }

    public List<Card> getOptimalDefenseOptions(Card attackCard, Suit trump) {
        List<Card> candidates = new ArrayList<>();
        
        // Кандидаты той же масти
        candidates.addAll(memory.getCardsBySuit(attackCard.getSuit()).stream()
                .filter(c -> c.getValue() > attackCard.getValue())
                .toList());
        
        // Козырные кандидаты
        if (attackCard.getSuit() != trump) {
            candidates.addAll(memory.getTrumpCards(trump).stream()
                    .filter(c -> c.getValue() > attackCard.getValue())
                    .toList());
        }
        
        return candidates.stream()
                .sorted(Comparator.comparingDouble(
                        c -> probabilityEngine.getProbability(c) * c.getValue()
                ))
                .toList();
    }

    public double calculateRiskLevel(Card attackCard, Suit trump) {
        double defenseProb = probabilityEngine.getDefenseProbability(attackCard, trump);
        double attackProb = probabilityEngine.getAttackProbability(attackCard, trump);
        return (1 - defenseProb) * attackProb * 100;
    }

    public List<Card> getHighPotentialCards(List<Card> hand, Suit trump) {
        return hand.stream()
                .filter(c -> probabilityEngine.getProbability(c) > 0.5)
                .sorted(Comparator.comparingDouble(
                        c -> calculateCardPotential(c, trump)
                ))
                .toList();
    }

    private double calculateCardPotential(Card card, Suit trump) {
        return card.getValue() * 
               (card.isTrump(trump) ? 1.5 : 1) * 
               probabilityEngine.getProbability(card);
    }

    public void updateGameState(List<Card> tableCards, int deckSize) {
        currentState.update(tableCards, deckSize, 
                probabilityEngine.getTrumpThreatLevel(currentState.trump));
    }

    // Вложенный класс для состояния игры
    private class GameState {
        private List<Card> table;
        private int deckSize;
        private double trumpThreat;
        
        public void update(List<Card> table, int deckSize, double trumpThreat) {
            this.table = new ArrayList<>(table);
            this.deckSize = deckSize;
            this.trumpThreat = trumpThreat;
        }
        
        public boolean isEndgame() {
            return deckSize == 0 && table.size() > 2;
        }
        
        public boolean isHighPressure() {
            return table.size() > 3 || trumpThreat > 0.6;
        }
    }

    // Методы для стратегических решений
    public boolean shouldTakeTrick(List<Card> attackCards, Suit trump) {
        if (attackCards.isEmpty()) return false;
        
        Card highest = getMostDangerousCard(attackCards, trump);
        return probabilityEngine.getDefenseProbability(highest, trump) < 0.4;
    }

    public boolean isCardViable(Card card, Suit trump) {
        return probabilityEngine.getProbability(card) > 0.3 &&
               (card.isTrump(trump) || 
                memory.getKnownValues(card.getSuit()).size() < 5);
    }

    public Set<Card> predictOpponentCards(Suit suit) {
        Set<Card> possible = new HashSet<>();
        for (int value = 6; value <= 14; value++) {
            Card card = new Card(value, suit);
            if (!memory.wasPlayed(card)) {
                possible.add(card);
            }
        }
        return possible;
    }

    public double getHandSynergy(List<Card> hand, Suit trump) {
        Map<Integer, Long> valueCounts = hand.stream()
                .collect(Collectors.groupingBy(Card::getValue, Collectors.counting()));
        
        double synergy = 0;
        for (Long count : valueCounts.values()) {
            synergy += count * (count - 1) * 10; // Бонус за повторяющиеся значения
        }
        
        long trumpCount = hand.stream()
                .filter(c -> c.isTrump(trump))
                .count();
        synergy += trumpCount * 5; // Бонус за козыри
        
        return synergy;
    }

    public void logAnalysis() {
        System.out.println("=== Анализ игры ===");
        System.out.printf("Оставшиеся карты: %d%n", probabilityEngine.getTotalUnknown());
        System.out.printf("Угроза козырей: %.2f%%%n", probabilityEngine.getTrumpThreatLevel(currentState.trump) * 100);
        System.out.printf("Риск текущей атаки: %.2f%%%n", calculateRiskLevel(currentState.table.get(0), currentState.trump));
    }
}
