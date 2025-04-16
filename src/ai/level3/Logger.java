package ai.level3;

public class Logger {
    public static void logThoughtProcess(String... thoughts) {
        System.out.println("== Процесс принятия решения ==");
        for (String thought : thoughts) {
            System.out.println(">> " + thought);
        }
    }

    public static void logProbability(String message, double probability) {
        System.out.printf("[Вероятность] %s: %.2f%%%n", message, probability * 100);
    }
}
