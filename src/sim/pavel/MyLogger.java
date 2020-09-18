package sim.pavel;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//Я так понимаю, что это utility класс такой. Тогда не нужно создавать его объекты и лучше запретить это делать,
//сделав единственный конструктор приватным
public class MyLogger {
    private static final String logFilePath = "test\\LogFile.txt";
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static FileHandler fileHandler;

    private static int excludedStringsWithSpacesCount = 0;
    private static final long start = System.currentTimeMillis();

    static {
        try {
            fileHandler = new FileHandler(logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new SimpleFormatter());
    }

    //сделать единственный конструктор приватным
    private MyLogger() {
    }

    public static long getElapsedTime() {
        return (System.currentTimeMillis() - start) / 1000;
    }

    public static void incrementExcludedStringsWithSpacesCount() {
        excludedStringsWithSpacesCount++;
    }

    public static int getExcludedStringsWithSpacesCount() {
        return excludedStringsWithSpacesCount;
    }

    public static String getLogFilePath() {
        return logFilePath;
    }

    public static Logger log() {
        return logger;
    }
}
