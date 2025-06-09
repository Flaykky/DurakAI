package ai;

import game.Card;
import game.Player;
import game.Game;
import ai.level3.memory.CardMemory;

public class Logger {
    private final String playerName;
    private final boolean debugMode = true;

    public Logger(String playerName) {
        this.playerName = playerName;
    }

    public void log(String message) {
        if (debugMode) {
            System.out.println("[" + playerName + "] " + message);
        }
    }

    public void logError(String message) {
        System.err.println("[ERROR " + playerName + "] " + message);
    }

    public void logGameState(Game game) {
        if (!debugMode) return;
        
        System.out.println("\n=== Game State ===");
        System.out.println("Trump: " + game.getDeck().getTrumpSuit());
        System.out.println("Cards in deck: " + game.getDeck().remainingCards());
        
        for (Player player : game.getPlayers()) {
            System.out.print(player.getName() + ": ");
            for (Card card : player.getHand()) {
                System.out.print(card + " ");
            }
            System.out.println();
        }
        
        System.out.println("Cards on table: ");
        for (Card card : game.getCardsOnTable()) {
            System.out.print(card + " ");
        }
        System.out.println("\n=====================\n");
    }

    public void logAttackDecision(Card card, boolean decision) {
        if (debugMode) {
            System.out.println("Attack decision " + card + ": " + 
                (decision ? "CONTINUE" : "STOP"));
        }
    }

    public void logDefenseDecision(Card attackCard, Card defendCard) {
        if (debugMode && defendCard != null) {
            System.out.println("Defend with " + defendCard + " against " + attackCard);
        }
    }

    public void logMemoryState(CardMemory memory) {
        if (!debugMode) return;
        
        System.out.println("=== Player Memory ===");
        System.out.println("Known cards: " + memory.getKnownCards());
        System.out.println("Played cards: " + memory.getPlayedCards());
        System.out.println("===================");
    }

    public void logStrategyDecision(String strategy, String decision) {
        if (debugMode) {
            System.out.println("[" + strategy + "] " + decision);
        }
    }

    public void logProbability(String card, double probability) {
        if (debugMode) {
            System.out.printf("Probability of having %s: %.2f%%\n", card, probability * 100);
        }
    }
}
