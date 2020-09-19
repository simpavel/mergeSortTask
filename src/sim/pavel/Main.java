package sim.pavel;

import java.util.Arrays;

import static sim.pavel.MyLogger.log;

public class Main {
    //TODO сделать !инструкцию по запуску для задания

    public static void main(String[] args) {

        long programStart = System.currentTimeMillis();
        System.out.println("Logger name: " + log().getName() + "\nProgram starts with arguments: " + Arrays.toString(args));

        Properties properties = Properties.fromArgs(args);

        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName()); //производим слияние

        System.out.println("Program took " + ((System.currentTimeMillis() - programStart) / 1000) + " seconds to " +
                "execute. \n");
    }
}
