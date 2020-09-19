package sim.pavel;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

//Я так понимаю, что это utility класс такой. Тогда не нужно создавать его объекты и лучше запретить это делать,
//сделав единственный конструктор приватным
class MyLogger {
    private static final String logFilePath = "LogFile.txt";
    private static final Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(logFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.addHandler(fileHandler);
        fileHandler.setFormatter(new SimpleFormatter());
    }

    //сделали единственный конструктор приватным
    private MyLogger() {
    }

    static Logger log() {
        return logger;
    }
}
