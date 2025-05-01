package ai.level3.memory;

import game.Card;
import game.Card.Suit;
import java.util.*;

public class CardMemory {
    private final Set<Card> knownCards = new HashSet<>();
    private final Map<Suit, Set<Integer>> suitValues = new HashMap<>();

    public void remember(Card card) {
        knownCards.add(card);
        suitValues.computeIfAbsent(card.getSuit(), k -> new HashSet<>())
                 .add(card.getValue());
    }

    public boolean wasPlayed(Card card) {
        return knownCards.contains(card);
    }

    public Set<Integer> getKnownValues(Suit suit) {
        return suitValues.getOrDefault(suit, Collections.emptySet());
    }

    // Новый метод для получения козырных карт
    public List<Card> getTrumpCards(Suit trump) {
        return knownCards.stream()
                .filter(c -> c.getSuit() == trump)
                .sorted(Comparator.comparingInt(Card::getValue))
                .toList();
    }

    public Set<Card> getKnownCards() {
        return Collections.unmodifiableSet(knownCards);
    }
}