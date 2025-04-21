# DurakAI

Durak is a popular Russian card game where the objective is to avoid being the last player with cards. This project is a Java implementation of Durak featuring an AI bot with multiple difficulty levels, capable of making strategic decisions based on game analysis.


## terminal set up
If instead of symbols '♤♡♧♢' you see '?' in the terminal, use this command:

```powershell
chcp 65001
```


## Features
- **Fully functional Durak game in Java**
- **AI bot with four levels of intelligence**
- **Logging of AI decision-making process**
- **Customizable rules and deck settings**

## AI Levels
1. **Basic AI** – Covers or attacks if possible without any strategy.
2. **Intermediate AI** – Makes simple decisions such as covering with the lowest available card.
3. **Advanced AI** – Implements strategic decision-making:
   - Decides whether to cover or take a card based on value.
   - Chooses between using a small trump or a high non-trump card.
   - Determines whether to throw in extra cards based on known opponent weaknesses.
   - Logs decisions, e.g., _"I need to cover Queen ♤, but my only trump is Ace ♡, so I take the card."_
4. **Expert AI** – In addition to advanced AI features:
   - Remembers all played cards.
   - Simulates possible end-game scenarios to find a winning strategy.
   - Predicts opponent hands based on observed gameplay.

## Game Rules
- The game is played with a deck of 36 cards (6–Ace in each suit).
- Players take turns attacking and defending.
- The trump suit is determined at the start of the game.
- Players can throw in additional cards matching rank during an attack.
- The defender must either cover all cards or pick up.
- The game continues until one player runs out of cards; the last remaining player is the "durak."

## AI Decision-Making Logic
- **Covering:**
  - If possible, cover with a lower-value card.
  - Use a trump card only when necessary.
- **Throwing in:**
  - If the opponent has taken a card of a particular suit, prioritize throwing more of that suit.
  - Ensure thrown-in cards don’t backfire.
- **Endgame Strategy:**
  - Keep track of played cards.
  - Predict opponent’s possible moves.
  - Choose a winning sequence when possible.

## Logging System
The AI logs its decisions in a structured format, e.g.:
```
[AI BOT LVL?] I need to cover Queen ♠. My lowest option is 10♠, but I have a small trump 7♡.
[AI BOT LVL?] Using 10♠ to preserve my trump for later.
[AI BOT LVL?] Opponent picked up 8♣. I should throw in 9♣.
```
This allows for debugging and improvement of AI logic.

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/Flaykky/DurakAI
   ```
2. Compile and run the project:
   ```sh
   javac src/Main.java
   java Main
   ```

## Future Improvements
- Enhancing AI memory and strategy for better end-game planning.
- Adding multiplayer support.
- Implementing a graphical interface.

