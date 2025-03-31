// src/game/Card.java
package game;

public class Card implements Comparable<Card> {
    public enum Suit {
        SPADES('♤'), HEARTS('♡'), DIAMONDS('♢'), CLUBS('♧');
        
        private final char symbol;
        Suit(char symbol) { this.symbol = symbol; }
        public char getSymbol() { return symbol; }
    }

    private final int value; // 6-14 (6 до туза)
    private final Suit suit;

    public Card(int value, Suit suit) {
        if (value < 6 || value > 14) {
            throw new IllegalArgumentException("Значение карты должно быть от 6 до 14");
        }
        this.value = value;
        this.suit = suit;
    }

    public int getValue() { return value; }
    public Suit getSuit() { return suit; }

    @Override
    public String toString() {
        return getValueSymbol() + suit.getSymbol();
    }

    public String getValueSymbol() {
        return value <= 10 ? 
            String.valueOf(value) : 
            switch(value) {
                case 11 -> "J";
                case 12 -> "Q";
                case 13 -> "K";
                case 14 -> "A";
                default -> "?";
            };
    }

    public boolean isTrump(Suit trump) {
        return suit == trump;
    }

    public boolean canBeat(Card other, Suit trump) {
        if (this.suit == other.suit) {
            return this.value > other.value;
        }
        return isTrump(trump) && !other.isTrump(trump);
    }

    public boolean sameValue(Card other) {
        return this.value == other.value;
    }

    public boolean sameSuit(Card other) {
        return this.suit == other.suit;
    }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.value, other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card other = (Card) obj;
        return value == other.value && suit == other.suit;
    }

    @Override
    public int hashCode() {
        return 31 * value + suit.hashCode();
    }

    // Статические методы для проверки условий
    public static boolean isValidValue(int value) {
        return value >= 6 && value <= 14;
    }

    public static boolean isValidSuit(char symbol) {
        for (Suit suit : Suit.values()) {
            if (suit.getSymbol() == symbol) {
                return true;
            }
        }
        return false;
    }

    public static Suit getSuitFromSymbol(char symbol) {
        for (Suit suit : Suit.values()) {
            if (suit.getSymbol() == symbol) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Неверный символ масти: " + symbol);
    }

    // Для отладки
    public String toFullString() {
        return String.format("%s %-8s (%d)",
            getValueSymbol(),
            suit.name(),
            value);
    }
}
