package ai;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Простой логгер для ИИ в игре "Дурак".
 * Поддерживает уровни логирования: DEBUG, INFO, WARN, ERROR.
 */
public class Logger {
    // Уровни логирования
    public enum Level {
        OFF, ERROR, WARN, INFO, DEBUG
    }

    // Текущий уровень логирования
    private static Level logLevel = Level.INFO;

    // Формат даты и времени
    private static final DateTimeFormatter formatter = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Устанавливает уровень логирования.
     * @param level новый уровень
     */
    public static void setLogLevel(Level level) {
        logLevel = level != null ? level : Level.OFF;
    }

    /**
     * Логирует сообщение уровня DEBUG.
     * @param source источник сообщения (например, имя класса)
     * @param message текст сообщения
     */
    public static void debug(String source, String message) {
        log(source, message, Level.DEBUG);
    }

    /**
     * Логирует сообщение уровня INFO.
     * @param source источник сообщения
     * @param message текст сообщения
     */
    public static void info(String source, String message) {
        log(source, message, Level.INFO);
    }

    /**
     * Логирует сообщение уровня WARN.
     * @param source источник сообщения
     * @param message текст сообщения
     */
    public static void warn(String source, String message) {
        log(source, message, Level.WARN);
    }

    /**
     * Логирует сообщение уровня ERROR.
     * @param source источник сообщения
     * @param message текст сообщения
     * @param throwable исключение (опционально)
     */
    public static void error(String source, String message, Throwable throwable) {
        log(source, message, Level.ERROR);
        if (throwable != null) {
            throwable.printStackTrace();
        }
    }

    /**
     * Общий метод логирования.
     * @param source источник
     * @param message текст
     * @param level уровень логирования
     */
    private static void log(String source, String message, Level level) {
        if (logLevel == Level.OFF || level.ordinal() > logLevel.ordinal()) {
            return; // Выход, если уровень логирования ниже текущего
        }

        String timestamp = LocalDateTime.now().format(formatter);
        String threadName = Thread.currentThread().getName();
        String logMessage = String.format("[%s] [%s] [%s] %s: %s", 
            timestamp, threadName, level, source, message);

        switch (level) {
            case DEBUG:
                System.out.println("\u001B[34m" + logMessage + "\u001B[0m"); // Синий
                break;
            case INFO:
                System.out.println("\u001B[32m" + logMessage + "\u001B[0m"); // Зеленый
                break;
            case WARN:
                System.out.println("\u001B[33m" + logMessage + "\u001B[0m"); // Желтый
                break;
            case ERROR:
                System.err.println("\u001B[31m" + logMessage + "\u001B[0m"); // Красный
                break;
            default:
                System.out.println(logMessage);
        }
    }

    /**
     * Возвращает имя класса, вызвавшего логгер.
     * @return имя класса
     */
    private static String getCallerClass() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 2; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            if (!element.getClassName().equals(Logger.class.getName())) {
                return element.getClassName();
            }
        }
        return "Unknown";
    }
}
