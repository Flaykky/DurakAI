package main;

import ai.Logger;
import ai.Logger.Level;
import ai.AIPlayer1lvl;
import ai.AIPlayer2lvl;
import game.Card;
import game.Game;
import game.*;
import game.Player;
import game.HumanPlayer;
import game.Card.Suit;
import game.Game.GameStateListener;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static boolean logToFile = false;
    private static PrintStream fileLogger = null;

    public static void main(String[] args) {
        if (args.length == 0 || Arrays.asList(args).contains("--help")) {
            showHelp();
            return;
        }

        // Настройка логгирования
        if (Arrays.asList(args).contains("--logFile")) {
            logToFile = true;
            setupFileLogger();
        }

        // Определение игроков
        Player player1 = createPlayer(args[0], "Player 1");
        Player player2 = createPlayer(args[1], "Player 2");

        if (player1 == null || player2 == null) {
            System.out.println("Invalid player configuration. Use --help for help.");
            return;
        }

        // Создание игры
        Game game = new Game(player1, player2);
        game.addGameStateListener(new Game.GameStateListener() {
            @Override
            public void onGameStarted() {
                log("Game started");
            }

            @Override
            public void onGameStateChanged(Game game) {
                log("Turn changed");
                printGameState(game);
            }

            @Override
            public void onCardPlayed(Player player, Card card) {
                log(player.getName() + " played " + card);
            }

            @Override
            public void onGameEnded(Player winner) {
                log("Game over. Winner: " + winner.getName());
                System.out.println("\nGame Over!");
                System.out.println("Winner: " + winner.getName());
                printFinalHands(game.getPlayers());
            }
        });

        // Запуск игры
        game.startGame();
        playGame(game);
    }

    private static Player createPlayer(String type, String defaultName) {
        switch (type.toLowerCase()) {
            case "1lvl":
                return new AIPlayer1lvl(defaultName);
            case "2lvl":
                return new AIPlayer2lvl(defaultName);
            case "player":
                return new HumanPlayer(defaultName);
            default:
                System.out.println("Unknown player type: " + type);
                return null;
        }
    }

    private static void showHelp() {
        System.out.println("Usage: java Main <player1> <player2> [options]");
        System.out.println("Players:");
        System.out.println("  1lvl - AI Level 1");
        System.out.println("  2lvl - AI Level 2");
        System.out.println("  player - Human player");
        System.out.println("Options:");
        System.out.println("  --logFile - Save logs to file");
        System.out.println("  --help - Show this help");
        System.out.println("\nExamples:");
        System.out.println("  java Main 1lvl 2lvl --logFile");
        System.out.println("  java Main player 2lvl");
    }

    private static void setupFileLogger() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File logFile = new File("duruak_log_" + timestamp + ".log");
            fileLogger = new PrintStream(new FileOutputStream(logFile));
            
            // Перехватываем стандартный вывод
            System.setOut(new PrintStream(new OutputStream() {
                @Override
                public void write(int b) {
                    System.out.write(b);
                    fileLogger.write(b);
                }
            }));
            
            System.out.println("Logging to file: " + logFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            System.err.println("Failed to create log file: " + e.getMessage());
        }
    }

    private static void log(String message) {
        if (logToFile && fileLogger != null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            fileLogger.printf("[%s] %s%n", timestamp, message);
        }
    }

    private static void printGameState(Game game) {
        clearConsole();
        
        System.out.println("=== DURAK GAME ===");
        System.out.println("Trump Suit: " + game.getDeck().getTrumpSuit().getSymbol());
        System.out.println("Remaining cards in deck: " + game.getDeck().remainingCards());
        System.out.println();

        for (Player player : game.getPlayers()) {
            System.out.print(player.getName() + "'s hand (" + player.getHandSize() + " cards): ");
            printHand(player.getHand(), player.isHuman());
            System.out.println();
        }

        System.out.println("\nCards on table:");
        printHand(game.getCardsOnTable(), true);
        System.out.println("\nCurrent phase: " + (game.isAttackingPhase() ? "Attack" : "Defense"));
        System.out.println("Attacker: " + game.getAttacker().getName());
        System.out.println("Defender: " + game.getDefender().getName());
        System.out.println();
    }

    private static void printFinalHands(List<Player> players) {
        System.out.println("\nFinal hands:");
        for (Player player : players) {
            System.out.print(player.getName() + ": ");
            printHand(player.getHand(), true);
        }
    }

    private static void printHand(List<Card> cards, boolean reveal) {
        List<Card> displayCards = new ArrayList<>(cards);
        
        if (!reveal) {
            displayCards = new ArrayList<>();
            for (int i = 0; i < cards.size(); i++) {
                Card hidden = new Card(Card.Suit.SPADE, Card.Rank.SIX);
                hidden.setFaceUp(false);
                displayCards.add(hidden);
            }
        }

        if (displayCards.isEmpty()) {
            System.out.println("Empty");
            return;
        }

        StringBuilder[] lines = new StringBuilder[3];
        for (int i = 0; i < 3; i++) {
            lines[i] = new StringBuilder();
        }

        for (Card card : displayCards) {
            if (card.isFaceUp()) {
                lines[0].append(card.getRank()).append(card.getSuit().getSymbol()).append(" ");
                lines[1].append("──── ").append(" ");
                lines[2].append("    ").append(" ");
            } else {
                lines[0].append("[??] ");
                lines[1].append("──── ");
                lines[2].append("    ");
            }
        }

        for (StringBuilder line : lines) {
            System.out.println(line);
        }
    }

    private static void playGame(Game game) {
        Scanner scanner = new Scanner(System.in);
        
        while (!game.isGameOver()) {
            Player currentPlayer = game.getCurrentPlayer();
            
            if (currentPlayer.isHuman()) {
                handleHumanTurn((HumanPlayer) currentPlayer, game, scanner);
            } else {
                // Для ИИ просто вызываем playCard или defendCard
                if (game.isAttackingPhase()) {
                    Card card = currentPlayer.playCard(game, game.getCardsOnTable());
                    if (card != null) {
                        game.playCard(currentPlayer, card);
                    }
                } else {
                    Card attackCard = game.getCardsOnTable().isEmpty() ? null : game.getCardsOnTable().get(0);
                    if (attackCard != null) {
                        Card defendCard = currentPlayer.defendCard(game, attackCard);
                        if (defendCard != null) {
                            game.defendCard(currentPlayer, defendCard);
                        }
                    }
                }
            }
            
            game.nextTurn();
        }
        
        scanner.close();
    }

    private static void handleHumanTurn(HumanPlayer player, Game game, Scanner scanner) {
        System.out.println("\nYour turn, " + player.getName() + "!");
        System.out.println("Your hand:");
        printHand(player.getHand(), true);
        
        if (game.isAttackingPhase()) {
            System.out.println("\nChoose a card to attack (number), or 0 to surrender:");
            List<Card> playableCards = player.getPlayableCards(game.getCardsOnTable());
            for (int i = 0; i < playableCards.size(); i++) {
                System.out.println((i + 1) + ": " + playableCards.get(i));
            }
            
            int choice = getValidInt(scanner, 0, playableCards.size());
            if (choice == 0) {
                game.surrender(player);
                return;
            }
            
            game.playCard(player, playableCards.get(choice - 1));
        } else {
            Card attackCard = game.getCardsOnTable().get(0);
            System.out.println("\nYou need to defend against: " + attackCard);
            List<Card> beatableCards = player.getBeatableCards(attackCard, game.getDeck().getTrumpSuit());
            
            if (beatableCards.isEmpty()) {
                System.out.println("No cards to defend. You have to take the cards.");
                return;
            }
            
            System.out.println("Choose a card to defend (number):");
            for (int i = 0; i < beatableCards.size(); i++) {
                System.out.println((i + 1) + ": " + beatableCards.get(i));
            }
            
            int choice = getValidInt(scanner, 1, beatableCards.size());
            game.defendCard(player, beatableCards.get(choice - 1));
        }
    }

    private static int getValidInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ":");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number:");
            }
        }
    }

    private static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Ignore
        }
    }
}
