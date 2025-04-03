// src/ai/AIPlayer2lvl.java
package ai;

import game.Card;
import game.Player;
import game.Card.Suit;

import java.util.*;
import java.util.stream.Collectors;

public class AIPlayer2lvl extends AIPlayer1lvl {
    private final Set<Card> knownCards = new HashSet<>(); // Запомненные карты
    private final Map<Suit, Integer> opponentSuitCounts = new HashMap<>();
    public AIPlayer2lvl(String name) {
        super(name);
    }

    @Override
    public Card chooseAttackCard(Suit trump) {
        List<Card> candidates = getAttackCandidates(trump);
        if (candidates.isEmpty()) return null;

        Card selected = chooseOptimalAttackCard(candidates);
        log("Атакую оптимальной картой: " + selected);
        return selected;
    }

    private List<Card> getAttackCandidates(Suit trump) {
        return hand.stream()
                .filter(c -> !c.isTrump(trump) || getTrumpCards(trump).size() > 2)
                .collect(Collectors.toList());
    }

    private Card chooseOptimalAttackCard(List<Card> candidates) {
        return candidates.stream()
                .max(Comparator.comparingInt(c -> getSameValueCount(c.getValue())))
                .orElse(candidates.get(0));
    }

    private int getSameValueCount(int value) {
        return (int) hand.stream().filter(c -> c.getValue() == value).count();
    }

    @Override
    public Card chooseDefenseCard(Card attackCard, Suit trump) {
        List<Card> possible = getValidDefenseCards(attackCard, trump);
        if (possible.isEmpty()) return null;

        Card selected = chooseOptimalDefenseCard(attackCard, trump, possible);
        log("Выбрана карта для защиты: " + selected);
        return selected;
    }

    private List<Card> getValidDefenseCards(Card attackCard, Suit trump) {
        return hand.stream()
                .filter(c -> c.canBeat(attackCard, trump))
                .collect(Collectors.toList());
    }

    private Card chooseOptimalDefenseCard(Card attackCard, Suit trump, List<Card> possible) {
        // Сначала пытаемся использовать не козырные карты
        List<Card> nonTrump = possible.stream()
                .filter(c -> !c.isTrump(trump))
                .sorted(Comparator.comparingInt(Card::getValue))
                .toList();

        if (!nonTrump.isEmpty()) {
            return nonTrump.get(0);
        }

        // Если только козыри - используем минимальный козырь
        return possible.stream()
                .min(Comparator.comparingInt(Card::getValue))
                .orElse(null);
    }

    @Override
    public List<Card> chooseAdditionalCards(List<Card> attackCards, Suit trump) {
        int targetValue = attackCards.get(0).getValue();
        List<Card> candidates = getCardsByValue(targetValue);
        
        // Оставляем 1 карту для возможной защиты
        if (candidates.size() > 1) {
            candidates = candidates.subList(0, candidates.size() - 1);
        }
        
        log("Подкидываю " + candidates.size() + " карт со значением " + targetValue);
        return candidates;
    }

    // Методы анализа
    public void rememberCard(Card card) {
        knownCards.add(card);
    }

    private void updateOpponentAnalysis(Card card) {
        opponentSuitCounts.put(card.getSuit(),
                opponentSuitCounts.getOrDefault(card.getSuit(), 0) + 1);
    }

    @Override
    protected void log(String message) {
        if (DEBUG_LOG) {
            System.out.println("[AI2 " + name + "] " + message);
            logAnalysis();
        }
    }
    private void logAnalysis() {
        System.out.println("  Запомненные карты: " + knownCards.size());
        System.out.println("  Анализ противника: " + opponentSuitCounts);
    }
}