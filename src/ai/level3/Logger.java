package ai.level3;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    private static Logger instance;
    private FileWriter fileWriter;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public enum Level {
        INFO, DEBUG, ERROR
    }

    private Logger() {
        try {
            fileWriter = new FileWriter("game.log", true);
        } catch (IOException e) {
            System.err.println("Failed to create log file");
            e.printStackTrace();
        }
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(Level level, String message) {
        String logMessage = String.format("[%s] [%s] %s%n",
                dateFormat.format(new Date()),
                level.toString(),
                message);

        // Выводим в консоль
        System.out.print(logMessage);

        // Записываем в файл
        try {
            if (fileWriter != null) {
                fileWriter.write(logMessage);
                fileWriter.flush();
            }
        } catch (IOException e) {
            System.err.println("Failed to write to log file");
            e.printStackTrace();
        }
    }

    public void info(String message) {
        log(Level.INFO, message);
    }

    public void debug(String message) {
        log(Level.DEBUG, message);
    }

    public void error(String message) {
        log(Level.ERROR, message);
    }

    public void close() {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
