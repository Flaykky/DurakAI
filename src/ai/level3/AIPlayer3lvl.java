package ai.level3;

import game.Card;
import game.Card.Suit;
import ai.level3.analysis.GameAnalyzer;
import ai.level3.memory.CardMemory;
import ai.level3.utils.CardPatterns;
import ai.AIPlayer2lvl;


import java.util.List;
import java.util.logging.Logger;

public class AIPlayer3lvl extends AIPlayer2lvl {
    private static final Logger logger = Logger.getLogger(AIPlayer3lvl.class.getName());
    protected final CardMemory memory = new CardMemory();
    protected final GameAnalyzer analyzer;
    protected final CardPatterns patternDetector;

    public AIPlayer3lvl(String name) {
        super(name);
        this.analyzer = new GameAnalyzer();
        this.patternDetector = new CardPatterns();
    }

    @Override
    public Card chooseAttackCard(Suit trump) {
        List<Card> candidates = getStrategicAttackCards();
        if (candidates.isEmpty()) {
            return hand.get(0);
        }
        return candidates.get(0);
    }

    private List<Card> getStrategicAttackCards() {
        return hand.stream()
                .filter(c -> c.getValue() != 0)  // simple safety check
                .toList();
    }

    @Override
    protected void log(String message) {
        super.log(message);
        logMemoryStatus();
    }

    private void logMemoryStatus() {
        String logmessage = "Memory status: " + memory.getRememberedCards().size() + " cards remembered.";
        logger.info(logmessage);
    }

    @Override
    public void addCard(Card card) {
        super.addCard(card);
        memory.remember(card);
    }
}
