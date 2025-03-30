package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck;
    private Card.Suit trumpSuit;
    private int attackerIndex = 0;
    private final List<Card> table = new ArrayList<>(); // Карты на столе
    private boolean canAddCards = true; // Флаг для подкидывания

    public Game() {
        deck = new Deck();
        trumpSuit = deck.getTrumpCard().getSuit();
        initializePlayers();
        dealInitialCards();
    }

    private void dealInitialCards() {
        for (Player player : players) {
            drawCards(player, 6);
        }
    }

    private void drawCards(Player player, int maxCards) {
        while (player.getHand().size() < maxCards && !deck.isEmpty()) {
            player.addCard(deck.draw());
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!isGameOver()) {
            Player attacker = players.get(attackerIndex);
            Player defender = players.get((attackerIndex + 1) % players.size());

            System.out.println("\n=== Новый ход ===");
            System.out.println("Козырь: " + trumpSuit.getSymbol());
            displayStatus(attacker, defender);

            // Фаза атаки
            List<Card> attackCards = new ArrayList<>();
            if (table.isEmpty()) { // Первый ход
                Card firstCard = chooseAttackCard(attacker, scanner);
                attackCards.add(firstCard);
                table.add(firstCard);
                attacker.removeCard(firstCard);
                canAddCards = true;
            } else { // Подкидывание
                attackCards = handleAddingCards(attacker, scanner);
                attackCards.forEach(attacker::removeCard);
                table.addAll(attackCards);
            }

            // Фаза защиты
            if (!attackCards.isEmpty()) {
                boolean defenseSuccess = handleDefense(defender, attackCards, scanner);
                
                // Добор карт
                drawCards(attacker, 6);
                drawCards(defender, 6);
                
                if (defenseSuccess) {
                    table.clear();
                    attackerIndex = (attackerIndex + 1) % players.size();
                    canAddCards = false;
                } else {
                    defender.getHand().addAll(table);
                    table.clear();
                    canAddCards = false;
                }
            }
        }
        endGame(scanner);
    }

    private List<Card> handleAddingCards(Player attacker, Scanner scanner) {
        List<Card> possibleCards = attacker.getHand().stream()
            .filter(c -> canAddCard(c))
            .toList();
        
        if (possibleCards.isEmpty()) return new ArrayList<>();

        System.out.println("Подкидываем карты:");
        List<Card> selected = new ArrayList<>();
        if (attacker instanceof AIPlayer) {
            // Здесь будет логика ИИ
        } else {
            while (true) {
                System.out.println("Выберите карты для подкидывания (индексы через пробел, Enter - закончить):");
                String input = scanner.nextLine();
                if (input.isEmpty()) break;
                
                String[] indexes = input.split(" ");
                for (String s : indexes) {
                    int idx = Integer.parseInt(s);
                    Card card = attacker.getHand().get(idx);
                    if (canAddCard(card)) {
                        selected.add(card);
                    }
                }
            }
        }
        return selected;
    }

    private boolean canAddCard(Card card) {
        return table.isEmpty() || 
               table.stream().anyMatch(c -> c.getValue() == card.getValue());
    }

    private boolean handleDefense(Player defender, List<Card> attackCards, Scanner scanner) {
        List<Card> defenseCards = new ArrayList<>();
        for (Card attackCard : attackCards) {
            Card defenseCard = chooseDefenseCard(defender, attackCard, scanner);
            if (defenseCard == null) return false;
            defenseCards.add(defenseCard);
            defender.removeCard(defenseCard);
        }
        return true;
    }

    private Card chooseAttackCard(Player attacker, Scanner scanner) {
        if (attacker instanceof AIPlayer) {
            // Здесь будет логика ИИ
            return null;
        } else {
            return humanChooseCard(attacker, scanner);
        }
    }

    private Card chooseDefenseCard(Player defender, Card attackCard, Scanner scanner) {
        if (defender instanceof AIPlayer) {
            // Здесь будет логика ИИ
            return null;
        } else {
            return humanChooseDefense(defender, attackCard, scanner);
        }
    }

    private boolean isValidDefense(Card attack, Card defense) {
        return (defense.getSuit() == attack.getSuit() && defense.getValue() > attack.getValue()) ||
               (defense.getSuit() == trumpSuit && attack.getSuit() != trumpSuit);
    }

    private void displayStatus(Player attacker, Player defender) {
        System.out.println("Ходит: " + attacker.getName());
        System.out.println("Ваша рука: " + attacker.getHand());
        System.out.println("Карт у " + defender.getName() + ": " + defender.getHand().size());
        System.out.println("Карт в колоде: " + deck.size());
    }

    private boolean isGameOver() {
        return players.stream().anyMatch(p -> !p.hasCards()) && deck.isEmpty();
    }

    private void endGame(Scanner scanner) {
        scanner.close();
        Player winner = players.stream()
            .filter(p -> !p.hasCards())
            .findFirst()
            .orElse(null);
        
        if (winner != null) {
            System.out.println("Игра окончена! Победитель: " + winner.getName());
        } else {
            System.out.println("Ничья!");
        }
    }
}
