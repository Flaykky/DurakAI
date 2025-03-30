package ai;

import game.Card;
import game.Player;

public abstract class AIPlayer extends Player {
    public AIPlayer(String name) {
        super(name);
    }

    public abstract Card chooseAttackCard(Card.Suit trump);
    public abstract Card chooseDefenseCard(Card attackCard, Card.Suit trump);
}