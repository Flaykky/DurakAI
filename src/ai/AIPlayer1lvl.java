package ai;

import game.Card;
import game.Game;
import game.Player;

import java.util.List;

/**
 * AIPlayer1lvl — самый простой уровень ИИ.
 * Подкидывает/кроет первую подходящую карту без анализа.
 */
public class AIPlayer1lvl extends Player {

    public AIPlayer1lvl(String name) {
        super(name, false); // false = не человек
    }

    /**
     * Выбирает первую подходящую карту для атаки.
     * @param game текущее состояние игры
     * @param cardsOnTable карты на столе
     * @return карта для атаки или null, если нечего играть
     */
    @Override
    public Card playCard(Game game, List<Card> cardsOnTable) {
        List<Card> playableCards = getPlayableCards(cardsOnTable);
        if (playableCards.isEmpty()) {
            return null; // Сдаётся, если нет подходящих карт
        }
        return playableCards.get(0); // Всегда выбирает первую подходящую карту
    }

    /**
     * Выбирает первую подходящую карту для защиты.
     * @param game текущее состояние игры
     * @param attackCard атакующая карта
     * @return карта для защиты или null, если не может покрыть
     */
    @Override
    public Card defendCard(Game game, Card attackCard) {
        List<Card> beatableCards = getBeatableCards(attackCard, game.getDeck().getTrumpSuit());
        if (beatableCards.isEmpty()) {
            return null; // Сдаётся, если не может покрыть
        }
        return beatableCards.get(0); // Всегда выбирает первую подходящую карту
    }

    /**
     * Возвращает список карт, которые можно сыграть.
     * @param cardsOnTable карты на столе
     * @return список подходящих карт
     */
    @Override
    public List<Card> getPlayableCards(List<Card> cardsOnTable) {
        List<Card> playable = new java.util.ArrayList<>();

        if (cardsOnTable.isEmpty()) {
            // Если на столе нет карт, можно сыграть любую
            playable.addAll(getHand());
        } else {
            // Иначе ищем карты с тем же номиналом, что и на столе
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
        List<Card> beatable = new java.util.ArrayList<>();
        for (Card card : getHand()) {
            if (card.beats(attackCard, trumpSuit)) {
                beatable.add(card);
            }
        }
        return beatable;
    }
}
