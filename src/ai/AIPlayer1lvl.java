package ai;

import java.util.List;
import game.Card;
import game.Player;
import game.Game;
import game.Card.Suit;

public class AIPlayer1lvl extends Player {

    public AIPlayer1lvl(String name) {
        super(name, false); // false — не человек
    }

    /**
     * Атакующий ход: выбирает первую подходящую карту
     * @param game текущая игра
     * @param cardsOnTable карты на столе
     * @return карта для хода или null
     */
    @Override
    public Card playCard(Game game, List<Card> cardsOnTable) {
        List<Card> playableCards = getPlayableCards(cardsOnTable);

        if (!playableCards.isEmpty()) {
            // Выбираем первую подходящую карту
            return playableCards.get(0);
        }

        return null; // нечем ходить
    }

    /**
     * Защитный ход: выбирает первую карту, которая бьёт атакующую
     * @param game текущая игра
     * @param attackCard атакующая карта
     * @return карта для защиты или null
     */
    @Override
    public Card defendCard(Game game, Card attackCard) {
        Suit trumpSuit = game.getDeck().getTrumpSuit();
        List<Card> beatableCards = getBeatableCards(attackCard, trumpSuit);

        if (!beatableCards.isEmpty()) {
            // Выбираем первую подходящую карту
            return beatableCards.get(0);
        }

        return null; // нечем крыть
    }

    /**
     * Возвращает список карт, которые можно сыграть
     * @param cardsOnTable карты на столе
     * @return список возможных карт
     */
    public List<Card> getPlayableCards(List<Card> cardsOnTable) {
        List<Card> playable = new java.util.ArrayList<>();

        if (cardsOnTable.isEmpty()) {
            // Можно ходить любой картой
            playable.addAll(getHand());
        } else {
            // Ищем карты с тем же номиналом, что и на столе
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
     * Возвращает список карт, которые могут побить атакующую
     * @param attackCard атакующая карта
     * @param trumpSuit козырная масть
     * @return список возможных карт
     */
    public List<Card> getBeatableCards(Card attackCard, Suit trumpSuit) {
        List<Card> beatable = new java.util.ArrayList<>();
        for (Card card : getHand()) {
            if (card.beats(attackCard, trumpSuit)) {
                beatable.add(card);
            }
        }
        return beatable;
    }

    /**
     * Сортировка карт по козырям и масти
     */
    public void sortHand(Suit trumpSuit) {
        getHand().sort((c1, c2) -> {
            // Сначала козыри
            boolean c1Trump = c1.isTrump(trumpSuit);
            boolean c2Trump = c2.isTrump(trumpSuit);

            if (c1Trump && !c2Trump) return -1;
            if (!c1Trump && c2Trump) return 1;

            // Затем по масти
            int suitComparison = c1.getSuit().compareTo(c2.getSuit());
            if (suitComparison != 0) return suitComparison;

            // Затем по значению
            return c1.getRank().compareTo(c2.getRank());
        });
    }
}
