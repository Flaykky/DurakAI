package game;

public class Card implements Comparable<Card> {
    public enum Suit {
        SPADE("♤"), HEART("♡"), DIAMOND("♢"), CLUB("♧");
        
        private final String symbol;
        
        Suit(String symbol) {
            this.symbol = symbol;
        }
        
        public String getSymbol() {
            return symbol;
        }
    }
    
    public enum Rank {
        SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10),
        JACK(11), QUEEN(12), KING(13), ACE(14);
        
        private final int value;
        
        Rank(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
        
        @Override
        public String toString() {
            switch (this) {
                case JACK: return "J";
                case QUEEN: return "Q";
                case KING: return "K";
                case ACE: return "A";
                default: return String.valueOf(value);
            }
        }
    }
    
    private final Suit suit;
    private final Rank rank;
    private boolean faceUp;
    
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }
    
    public Suit getSuit() {
        return suit;
    }
    
    public Rank getRank() {
        return rank;
    }
    
    public boolean isFaceUp() {
        return faceUp;
    }
    
    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }
    
    public boolean isTrump(Suit trumpSuit) {
        return suit == trumpSuit;
    }
    
    public boolean beats(Card other, Suit trumpSuit) {
        if (other == null) return false;
        
        // Обе карты не показаны - нельзя сравнить
        if (!this.faceUp || !other.faceUp) return false;
        
        // Если масти совпадают или одна из карт козырная
        if (this.suit == other.suit || 
            (this.suit == trumpSuit || other.suit == trumpSuit)) {
            return this.rank.getValue() > other.rank.getValue();
        }
        return false;
    }
    
    @Override
    public int compareTo(Card other) {
        if (other == null) return 1;
        int suitComparison = this.suit.compareTo(other.suit);
        if (suitComparison != 0) {
            return suitComparison;
        }
        return this.rank.compareTo(other.rank);
    }
    
    @Override
    public String toString() {
        if (!faceUp) {
            return "[??]";
        }
        return String.format("[%s%s]", rank, suit.getSymbol());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Card card = (Card) obj;
        
        if (suit != card.suit) return false;
        return rank == card.rank;
    }
    
    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}