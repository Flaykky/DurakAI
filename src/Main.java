
import game.Game;
import ai.AIPlayer1lvl;
import ai.AIPlayer2lvl;
import game.Player;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите режим тестирования:");
        System.out.println("1 - ИИ 1 уровня vs ИИ 2 уровня");
        System.out.println("2 - Человек vs ИИ 1 уровня");
        System.out.println("3 - Человек vs ИИ 2 уровня");
        System.out.println("4 - ИИ 1 уровня vs ИИ 1 уровня");
        System.out.println("5 - ИИ 2 уровня vs ИИ 2 уровня");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Очистка буфера
        
        Game game = createGame(choice);
        game.play();
        scanner.close();
    }

    private static Game createGame(int choice) {
        switch(choice) {
            case 1:
                System.out.println("Запуск ИИ 1 уровня против ИИ 2 уровня");
                return new Game(
                    new AIPlayer1lvl("Бот1"),
                    new AIPlayer2lvl("Бот2")
                );
                
            case 2:
                System.out.println("Запуск игры: Человек vs ИИ 1 уровня");
                return new Game(
                    new Player("Человек"),
                    new AIPlayer1lvl("Бот1")
                );
                
            case 3:
                System.out.println("Запуск игры: Человек vs ИИ 2 уровня");
                return new Game(
                    new Player("Человек"),
                    new AIPlayer2lvl("Бот2")
                );
                
            case 4:
                System.out.println("Запуск ИИ 1 уровня vs ИИ 1 уровня");
                return new Game(
                    new AIPlayer1lvl("Бот1"),
                    new AIPlayer1lvl("Бот2")
                );
                
            case 5:
                System.out.println("Запуск ИИ 2 уровня vs ИИ 2 уровня");
                return new Game(
                    new AIPlayer2lvl("Бот1"),
                    new AIPlayer2lvl("Бот2")
                );
                
            default:
                System.out.println("Неверный выбор. Запуск по умолчанию: ИИ 1 vs ИИ 2");
                return new Game(
                    new AIPlayer1lvl("Бот1"),
                    new AIPlayer2lvl("Бот2")
                );
        }
    }
}