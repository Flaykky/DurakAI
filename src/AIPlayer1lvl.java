// src/ai/AIPlayer1lvl.java
package ai;

import game.Card;
import game.Player;
import game.Card.Suit;

import java.util.List;

public class AIPlayer1lvl extends Player {
    // Изменили модификатор на protected
    protected static final boolean DEBUG_LOG = true;

    public AIPlayer1lvl(String name) {
        super(name);
    }

    @Override
    public Card chooseAttackCard(Suit trump) {
        if (hand.isEmpty()) return null;
        
        Card selected = hand.get(0); // Минимальная карта
        
        log("Атакую минимальной картой: " + selected);
        return selected;
    }

    @Override
    public Card chooseDefenseCard(Card attackCard, Suit trump) {
        if (hand.isEmpty()) return null;

        Card bestCard = null;
        
        // Сначала ищем минимальную подходящую карту
        for (Card card : hand) {
            if (card.canBeat(attackCard, trump)) {
                if (bestCard == null || card.getValue() < bestCard.getValue()) {
                    bestCard = card;
                }
            }
        }
        
        if (bestCard != null) {
            log("Покрываю " + attackCard + " картой " + bestCard);
            return bestCard;
        }
        
        // Если нет подходящей карты - берем
        log("Не могу покрыть " + attackCard + " - беру карту");
        return null;
    }

    // Исправлено: убрана аннотация @Override
    public List<Card> chooseAdditionalCards(List<Card> attackCards, Suit trump) {
        // Подкидываем все карты с тем же значением
        int targetValue = attackCards.get(0).getValue();
        List<Card> candidates = getCardsByValue(targetValue);
        
        log("Подкидываю " + candidates.size() + " карт со значением " + targetValue);
        return candidates;
    }

// Изменили модификатор на protected
    protected void log(String message) {
        if (DEBUG_LOG) {
            System.out.println("[AI " + name + "] " + message);
        }
    }
}