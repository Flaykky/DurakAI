package ai.level3;

import game.Card;
import game.Card.Suit;
import ai.level3.analysis.GameAnalyzer;
import ai.level3.memory.CardMemory;
import ai.level3.utils.CardPatterns;
import ai.AIPlayer2lvl;

import java.util.List;

public class AIPlayer3lvl extends AIPlayer2lvl {
    protected final CardMemory memory = new CardMemory();
    protected final GameAnalyzer analyzer = new GameAnalyzer(memory);
    protected final CardPatterns patternDetector = new CardPatterns();

    public AIPlayer3lvl(String name) {
        super(name);
    }

    @Override
    public Card chooseAttackCard(Suit trump) {
        List<Card> candidates = getStrategicAttackCards(trump);
        Card selected = patternDetector.findBestPattern(candidates);
        
        logAnalysis("Атакую картой: " + selected);
        return selected;
    }

    private List<Card> getStrategicAttackCards(Suit trump) {
        return hand.stream()
                .filter(c -> analyzer.isValueSafe(c.getValue()))
                .filter(c -> !analyzer.isSuitDangerous(c.getSuit()))
                .toList();
    }

    @Override
    protected void log(String message) {
        super.log(message);
        logMemoryStatus();
    }

    private void logMemoryStatus() {
        System.out.println("  Запомнено карт: " + memory.playedCards.size());
        System.out.println("  Опасные масти: " + analyzer.getDangerousSuits());
    }

    @Override
    public void addCard(Card card) {
        super.addCard(card);
        memory.remember(card);
    }

    // Методы для анализа комбинаций
    private boolean hasWinningCombo() {
        return patternDetector.hasFlush(hand) || 
               patternDetector.hasStraight(hand);
    }
}