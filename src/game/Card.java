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
        this.value = value;
        this.suit = suit;
    }

    public int getValue() { return value; }
    public Suit getSuit() { return suit; }

    @Override
    public String toString() {
        String val = value <= 10 ? String.valueOf(value) : 
            switch(value) {
                case 11 -> "J";
                case 12 -> "Q";
                case 13 -> "K";
                case 14 -> "A";
                default -> "?";
            };
        return val + suit.getSymbol();
    }

    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.value, other.value);
    }
}