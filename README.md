# Durkai

A Java implementation of the classic Russian card game "Durak" (Fool) featuring multiple AI difficulty levels and ASCII card visuals.

## 📁 Project Structure

```
src/
├── main/                     # Application entry point
│   └── Main.java             # Main class with CLI interface
├── ai/                       # AI player implementations
│   ├── AIPlayer1lvl.java     # Level 1 AI (basic)
│   ├── AIPlayer2lvl.java     # Level 2 AI (intermediate)
│   └── level3/               # Level 3 AI (advanced)
│       ├── AIPlayer3lvl.java
│       ├── analysis/
│       │   ├── GameAnalyzer.java
│       │   └── ProbabilityEngine.java
│       ├── memory/
│       │   ├── CardMemory.java
│       │   └── OpponentModel.java
│       ├── strategy/
│       │   ├── DefenseStrategy.java
│       │   └── AttackStrategy.java
│       └── utils/
│           └── CardPatterns.java
│   └── Logger.java           # Centralized logging system
├── game/                     # Core game mechanics
│   ├── Card.java             # Card representation
│   ├── Deck.java             # Deck management
│   ├── Player.java           # Abstract player class
│   ├── HumanPlayer.java      # Human player implementation
│   └── Game.java             # Game logic and state management
└── utils/
    └── ConsoleRenderer.java  # ASCII rendering utilities
```


## 🚀 Getting Started

### Requirements
- Java 17+
- Git (for cloning repository)

### Installation
```bash
git clone https://github.com/yourusername/durak-game.git
cd durak-game
javac -d out src/**/*.java
```

## terminal set up
If instead of symbols '♤♡♧♢' you see '?' in the terminal, use this command:

```powershell
chcp 65001
```

## 🎮 Running the Game

### Game Modes
```bash
# AI vs AI with logging
java -cp out Main 1lvl 2lvl --logFile

# Human vs AI
java -cp out Main player 2lvl

# Help
java -cp out Main --help
```

### Command Line Arguments
| Mode                  | Command                          | Description                          |
|-----------------------|----------------------------------|--------------------------------------|
| AI vs AI              | `1lvl 2lvl`                      | Runs game between AI levels          |
| Human vs AI           | `player 2lvl`                    | Human plays against AI               |
| Logging               | `--logFile`                      | Creates .log file with game history  |
| Help                  | `--help`                         | Shows command line usage             |

## 🧠 AI System

### AI Levels
| Level | Strategy                          | Complexity |
|-------|-----------------------------------|------------|
| 1     | Plays first valid card            | Basic      |
| 2     | Uses minimal necessary cards      | Medium     |
| 3     | Memory + probability analysis     | Advanced   |

### Level 1 AI
- **Attack**: Plays first valid card
- **Defense**: Plays first card that beats attacker
- **No Strategy**: No consideration of trump cards or card value

### Level 2 AI
- **Attack**: Plays lowest possible card
- **Defense**: 
  - Prioritizes non-trump cards
  - Uses lowest valid trump as last resort
- **Efficiency**: Preserves high-value cards

### Level 3 AI (Planned)
- **Memory System**: Tracks played cards
- **Probability Engine**: Calculates win probabilities
- **Opponent Modeling**: Learns from human patterns
- **Advanced Strategy**: 
  - Sacrifices low cards when needed
  - Saves trumps for critical moments

## 📜 Logging System

### Log Levels
| Level   | Color  | Purpose                           |
|---------|--------|-----------------------------------|
| DEBUG   | Blue   | Detailed AI decision-making       |
| INFO    | Green  | Game state changes                |
| WARN    | Yellow | Potential issues                  |
| ERROR   | Red    | Critical failures                 |

### Log File Example
```
[2023-10-05 14:30:41] [main] [DEBUG] AIPlayer2lvl: Selected defense card: [Q♧]
[2023-10-05 14:31:12] [main] [INFO] Game: Turn changed
[2023-10-05 14:31:15] [main] [ERROR] Game: Invalid card selection. Try again.
```

## 🃏 Game Mechanics

### Card Representation
- **Suits**: ♤(Spades), ♡(Hearts), ♢(Diamonds), ♧(Clubs)
- **Ranks**: 6-Ace (6,7,8,9,10,J,Q,K,A)
- **Trump Suit**: Determines card hierarchy

### Game Flow
1. **Initialization**: 
   - 36-card deck
   - Initial draw of 6 cards
   - Trump suit determined by first card

2. **Turn Structure**:
   - **Attack Phase**: Play cards of matching rank
   - **Defense Phase**: Must beat attacking card
   - **Card Draw**: Players refill hand to 6 cards

3. **Winning Conditions**:
   - First player to empty hand
   - Opponent unable to defend

## 🧪 Development Guide

### Contributing
1. Fork the repository
2. Create feature branch (`git checkout -b feature/ai-level3`)
3. Commit changes (`git commit -m 'Add level 3 AI'`)
4. Push branch (`git push origin feature/ai-level3`)
5. Open pull request

### Code Style
- Google Java Style Guide
- Javadoc for all public classes and methods
- Consistent use of ASCII card representation

### Testing
1. Unit tests for card comparison logic
2. Integration tests for game flow
3. AI performance benchmarks

## 📄 License
MIT License - see LICENSE file for details

## 🧑‍🤝‍🧑 Acknowledgments
- Original game concept: Traditional Russian "Durak"
- ASCII art inspiration: Classic card games
- AI design patterns: Game theory fundamentals
```

The markdown format makes it suitable for GitHub README files, documentation portals, or generating PDF documentation.
