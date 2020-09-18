package sim.pavel;

import java.util.Arrays;

import static sim.pavel.MyLogger.log;

public class Main {

    public static void main(String[] args) {
        System.out.println("Logger name: " + log().getName() + "\nProgram starts with arguments: " + Arrays.toString(args));
        System.out.println("String".compareTo("BB"));
        Properties properties = Properties.fromArgs(args);

        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName()); //производим слияние

        System.out.println("\nProgram took \"" + MyLogger.getElapsedTime()
                + "\"seconds to execute \n" + "We had \"" + MyLogger.getExcludedStringsWithSpacesCount() + "\""
                + " excluded elements due to unsorted input files. \nFor more information see logfile @ \""
                + MyLogger.getLogFilePath()
        );
    }
}
