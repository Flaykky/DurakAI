package game;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import game.Card.Suit;
import game.Card.Rank;
import game.Player;
import game.Deck;


public class Game {
    private static final int INITIAL_CARDS = 6;
    private final Deck deck = new Deck();
    private final List<Player> players = new ArrayList<>();
    private final List<Card> cardsOnTable = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private boolean gameOver = false;
    private boolean attackingPhase = true;
    private Player attacker;
    private Player defender;
    private final List<Player> activePlayers = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final List<GameStateListener> listeners = new ArrayList<>();
    private boolean firstTurn = true;
    
    public Game(Player player1, Player player2) {
        players.add(player1);
        players.add(player2);
        activePlayers.addAll(players);
    }
    
    public void startGame() {
        initializeHands();
        determineFirstAttacker();
        notifyGameStarted();
    }
    
    private void initializeHands() {
        for (Player player : players) {
            List<Card> initialCards = new ArrayList<>();
            for (int i = 0; i < INITIAL_CARDS; i++) {
                Card card = deck.drawCard();
                if (card != null) {
                    initialCards.add(card);
                }
            }
            player.addCards(initialCards);
        }
        
        // Сортировка рук по козырям
        for (Player player : players) {
            player.sortHand(deck.getTrumpSuit());
        }
    }
    
    private void determineFirstAttacker() {
        // Определяем игрока с наименьшим козырем
        Player minTrumpPlayer = null;
        Card minTrumpCard = null;
        
        for (Player player : players) {
            for (Card card : player.getHand()) {
                if (card.isTrump(deck.getTrumpSuit())) {
                    if (minTrumpCard == null || 
                        card.getRank().getValue() < minTrumpCard.getRank().getValue()) {
                        minTrumpCard = card;
                        minTrumpPlayer = player;
                    }
                }
            }
        }
        
        if (minTrumpPlayer != null) {
            currentPlayerIndex = players.indexOf(minTrumpPlayer);
        } else {
            // Если нет козырей, случайный выбор
            currentPlayerIndex = new Random().nextInt(players.size());
        }
        
        attacker = players.get(currentPlayerIndex);
        defender = getNextPlayer(attacker);
    }
    
    public void nextTurn() {
        if (gameOver) return;
        
        if (attackingPhase) {
            // Завершение фазы атаки
            if (cardsOnTable.isEmpty()) {
                // Если никто не сыграл, переход к следующему игроку
                endAttackPhase();
            } else {
                // Переключение на защиту
                attackingPhase = false;
                defender = getNextPlayer(attacker);
            }
        } else {
            // После защиты
            if (cardsOnTable.isEmpty()) {
                // Все карты покрыты, переход к следующему игроку
                endAttackPhase();
            } else {
                // Если есть непокрытые карты, игрок забирает их
                handleFailedDefense();
            }
        }
        
        checkForGameOver();
        notifyStateChanged();
    }
    
    private void endAttackPhase() {
        // Передача хода
        attacker = defender;
        defender = getNextPlayer(attacker);
        attackingPhase = true;
        cardsOnTable.clear();
        firstTurn = false;
        
        // Подбор карт
        dealCards();
    }
    
    private void handleFailedDefense() {
        // Защитник забирает карты
        defender.addCards(new ArrayList<>(cardsOnTable));
        cardsOnTable.clear();
        
        // Подбор карт
        dealCards();
        
        // Переключение на следующего игрока
        defender = getNextPlayer(defender);
        attacker = defender;
        attackingPhase = true;
    }
    
    private void dealCards() {
        for (Player player : activePlayers) {
            while (player.getHandSize() < INITIAL_CARDS && !deck.isEmpty()) {
                Card card = deck.drawCard();
                if (card != null) {
                    player.addCard(card);
                }
            }
            player.sortHand(deck.getTrumpSuit());
        }
    }
    
    public boolean playCard(Player player, Card card) {
        if (!isValidPlayerTurn(player) || !player.containsCard(card) || 
            !player.canPlayCard(card, deck.getTrumpSuit()) || 
            !canPlayThisCard(player, card)) {
            return false;
        }
        
        player.removeCard(card);
        cardsOnTable.add(card);
        notifyCardPlayed(player, card);
        return true;
    }
    
    private boolean isValidPlayerTurn(Player player) {
        return attackingPhase ? player == attacker : player == defender;
    }
    
    private boolean canPlayThisCard(Player player, Card card) {
        if (attackingPhase) {
            return cardsOnTable.isEmpty() || 
                   cardsOnTable.stream().anyMatch(c -> c.getRank() == card.getRank());
        } else {
            return cardsOnTable.size() == 1 && 
                   card.beats(cardsOnTable.get(0), deck.getTrumpSuit());
        }
    }
    
    public boolean defendCard(Player player, Card defendCard) {
        if (!isValidPlayerTurn(player) || defendingCard == null || 
            !player.containsCard(defendCard) || 
            !player.canDefend(cardsOnTable.get(0), defendCard, deck.getTrumpSuit())) {
            return false;
        }
        
        player.removeCard(defendCard);
        cardsOnTable.add(defendCard);
        notifyCardPlayed(player, defendCard);
        
        // Проверка завершения защиты
        if (cardsOnTable.size() == 2) {
            cardsOnTable.clear();
        }
        
        return true;
    }
    
    private void checkForGameOver() {
        for (Player player : players) {
            if (!player.hasCards()) {
                gameOver = true;
                notifyGameEnded(player);
                return;
            }
        }
        
        // Если колода пуста и игроки не могут продолжать
        if (deck.isEmpty() && !canContinueGame()) {
            gameOver = true;
            Player winner = determineWinner();
            notifyGameEnded(winner);
        }
    }
    
    private boolean canContinueGame() {
        for (Player player : activePlayers) {
            if (player.getHandSize() > 0) {
                return true;
            }
        }
        return false;
    }
    
    private Player determineWinner() {
        Player winner = null;
        int minCards = Integer.MAX_VALUE;
        
        for (Player player : players) {
            int cardCount = player.getHandSize();
            if (cardCount < minCards) {
                minCards = cardCount;
                winner = player;
            }
        }
        
        return winner;
    }
    
    public void surrender(Player player) {
        gameOver = true;
        Player winner = getNextPlayer(player);
        notifyGameEnded(winner);
    }
    
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }
    
    public boolean isAttackingPhase() {
        return attackingPhase;
    }
    
    public Player getAttacker() {
        return attacker;
    }
    
    public Player getDefender() {
        return defender;
    }
    
    public List<Card> getCardsOnTable() {
        return new ArrayList<>(cardsOnTable);
    }
    
    public Deck getDeck() {
        return deck;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean isFirstTurn() {
        return firstTurn;
    }
    
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    public Player getNextPlayer(Player currentPlayer) {
        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        return players.get(nextIndex);
    }
    
    public void addGameStateListener(GameStateListener listener) {
        listeners.add(listener);
    }
    
    public void removeGameStateListener(GameStateListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyGameStarted() {
        listeners.forEach(GameStateListener::onGameStarted);
    }
    
    private void notifyStateChanged() {
        listeners.forEach(listener -> listener.onGameStateChanged(this));
    }
    
    private void notifyCardPlayed(Player player, Card card) {
        listeners.forEach(listener -> listener.onCardPlayed(player, card));
    }
    
    private void notifyGameEnded(Player winner) {
        listeners.forEach(listener -> listener.onGameEnded(winner));
    }
    
    public interface GameStateListener {
        void onGameStarted();
        void onGameStateChanged(Game game);
        void onCardPlayed(Player player, Card card);
        void onGameEnded(Player winner);
    }


    /**
     * Обработка хода человека в фазе атаки
     */
    public boolean humanAttackTurn(Player humanPlayer, Card selectedCard) {
        if (!isValidPlayerTurn(humanPlayer) || !humanPlayer.containsCard(selectedCard)) {
            return false;
        }
        
        if (!humanPlayer.canPlayCard(selectedCard, deck.getTrumpSuit())) {
            return false;
        }
        
        return playCard(humanPlayer, selectedCard);
    }

    /**
     * Обработка хода человека в фазе защиты
     */
    public boolean humanDefendTurn(Player humanPlayer, Card attackCard, Card selectedCard) {
        if (!isValidPlayerTurn(humanPlayer) || !humanPlayer.containsCard(selectedCard)) {
            return false;
        }
        
        if (!humanPlayer.canDefend(attackCard, selectedCard, deck.getTrumpSuit())) {
            return false;
        }
        
        return defendCard(humanPlayer, selectedCard);
    }

    /**
     * Получение списка допустимых карт для атаки для человека
     */
    public List<Card> getHumanPlayableCards(Player humanPlayer) {
        return humanPlayer.getPlayableCards(cardsOnTable);
    }

    /**
     * Получение списка допустимых карт для защиты для человека
     */
    public List<Card> getHumanBeatableCards(Player humanPlayer, Card attackCard) {
        return humanPlayer.getBeatableCards(attackCard, deck.getTrumpSuit());
    }

    /**
     * Проверка, может ли человек сходить выбранной картой
     */
    public boolean canHumanPlayCard(Player humanPlayer, Card card) {
        return humanPlayer.canPlayCard(card, deck.getTrumpSuit()) && 
            humanPlayer.containsCard(card);
    }

    /**
     * Проверка, может ли человек покрыть атакующую карту
     */
    public boolean canHumanDefendCard(Player humanPlayer, Card attackCard, Card defendCard) {
        return defendCard != null && 
            humanPlayer.containsCard(defendCard) && 
            defendCard.beats(attackCard, deck.getTrumpSuit());
    }
}
