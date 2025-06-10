package ai;

import game.Card;
import game.Card.Suit;
import game.Game;
import game.Player;

import java.util.*;
import java.util.stream.Collectors;

import ai.Logger;
import ai.Logger.Level;

/**
 * AIPlayer2lvl — второй уровень ИИ.
 * Выбирает минимально необходимые карты для защиты и атаки.
 */
public class AIPlayer2lvl extends Player {

    public AIPlayer2lvl(String name) {
        super(name, false); // false = не человек
    }

    /**
     * Выбирает минимальную подходящую карту для атаки.
     * @param game текущее состояние игры
     * @param cardsOnTable карты на столе
     * @return карта для атаки или null, если нечего играть
     */
    @Override
    public Card playCard(Game game, List<Card> cardsOnTable) {
        List<Card> playableCards = getPlayableCards(cardsOnTable);
        if (playableCards.isEmpty()) {
            Logger.info("AIPlayer2lvl", "Нет подходящих карт для атаки");
            return null;
        }

        // Сортируем карты по рангу (минимальные сначала)
        playableCards.sort(Comparator.comparing(c -> c.getRank().getValue()));

        Card selectedCard = playableCards.get(0);
        Logger.debug("AIPlayer2lvl", "Выбрана карта для атаки: " + selectedCard);
        return selectedCard;
    }

    /**
     * Выбирает минимальную карту, которая может побить атакующую.
     * Старается не использовать козыри, если возможно.
     * @param game текущее состояние игры
     * @param attackCard атакующая карта
     * @return карта для защиты или null, если не может покрыть
     */
    @Override
    public Card defendCard(Game game, Card attackCard) {
        Suit trumpSuit = game.getDeck().getTrumpSuit();
        List<Card> beatableCards = getBeatableCards(attackCard, trumpSuit);

        if (beatableCards.isEmpty()) {
            Logger.info("AIPlayer2lvl", "Нет подходящих карт для защиты");
            return null;
        }

        // Разделяем карты на козырные и некозырные
        List<Card> nonTrumpCards = beatableCards.stream()
            .filter(c -> !c.isTrump(trumpSuit))
            .collect(Collectors.toList());
        
        List<Card> trumpCards = beatableCards.stream()
            .filter(c -> c.isTrump(trumpSuit))
            .collect(Collectors.toList());

        // Сортируем по рангу
        nonTrumpCards.sort(Comparator.comparing(c -> c.getRank().getValue()));
        trumpCards.sort(Comparator.comparing(c -> c.getRank().getValue()));

        Card selectedCard;

        // Сначала используем некозырные карты, если они есть
        if (!nonTrumpCards.isEmpty()) {
            selectedCard = nonTrumpCards.get(0);
        } else {
            selectedCard = trumpCards.get(0);
            Logger.debug("AIPlayer2lvl", "Используется козырная карта для защиты: " + selectedCard);
        }

        return selectedCard;
    }

    /**
     * Возвращает список карт, которые можно сыграть.
     * @param cardsOnTable карты на столе
     * @return список подходящих карт
     */
    @Override
    public List<Card> getPlayableCards(List<Card> cardsOnTable) {
        List<Card> playable = new ArrayList<>();

        if (cardsOnTable.isEmpty()) {
            playable.addAll(getHand());
        } else {
            for (Card card : getHand()) {
                for (Card tableCard : cardsOnTable) {
                    if (card.getRank() == tableCard.getRank()) {
                        playable.add(card);
                        break;
                    }
                }
            }
        }

        return playable;
    }

    /**
     * Возвращает список карт, которые могут побить атакующую карту.
     * @param attackCard атакующая карта
     * @param trumpSuit козырная масть
     * @return список подходящих карт
     */
    @Override
    public List<Card> getBeatableCards(Card attackCard, Card.Suit trumpSuit) {
        List<Card> beatable = new ArrayList<>();
        for (Card card : getHand()) {
            if (card.beats(attackCard, trumpSuit)) {
                beatable.add(card);
            }
        }
        return beatable;
    }
}
