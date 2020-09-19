package sim.pavel;

import java.util.ArrayList;
import java.util.List;

import static sim.pavel.MyLogger.log;

//в этом классе храним данные по входным и выходному файлам (входных может быть более одного)
public class Properties {
    //Режим сортировки по умолчанию - возрастающий
    private final boolean descendingSortOrder;
    //Вид данных по умолчанию - String
    private final boolean dataTypeInt;
    //Список путей к входным файлам
    private final List<String> inputFileNames;
    private final String outputFileName;

    private Properties(boolean descendingSortOrder, boolean dataTypeInt,
                       List<String> inputFileNames, String outputFileName) {
        this.descendingSortOrder = descendingSortOrder;
        this.dataTypeInt = dataTypeInt;
        this.inputFileNames = inputFileNames;
        this.outputFileName = outputFileName;
    }

    static Properties fromArgs(String[] args) {
        if (args.length < 4) {
            log().severe("args.length has to be at least 3 (i.e. \"-i, output.txt, input.txt\")");
            System.exit(1);
        }

        //Устанавливаем descendingOrder в соответствии с полученным параметром
        boolean descendingSortOrder;
            descendingSortOrder = args[0].equals("-d");

        //Смотрим, содержит ли первый аргумент -a или -d:
        //(т.к. это не обязательный параметр) если этого параметра не было указано, то следующий параметр
        // будет в индексе args[0]
        int argumentIndex = args[0].matches("^-[a|d]$") ? 1 : 0;
        boolean dataTypeIsInt = false;
        String outputFileName = "";

        if (args[argumentIndex].matches("^-[i|s]$")) {
            dataTypeIsInt = (args[argumentIndex].equals("-i"));
            argumentIndex++;

            outputFileName = args[argumentIndex];
            argumentIndex++;
        } else {
            log().severe("Invalid program arguments detected: " + args[argumentIndex] + " must be -i or -s");
            System.exit(1);
        }

        List<String> inputFileNames = new ArrayList<>();
        //все остальные аргументы - пути к входным файлам
        for (; argumentIndex < args.length; argumentIndex++) {
            inputFileNames.add(args[argumentIndex]);
        }

        return new Properties(descendingSortOrder, dataTypeIsInt, inputFileNames, outputFileName);
    }

    boolean isDescendingSortOrder() {
        return descendingSortOrder;
    }

    boolean isDataTypeInt() {
        return dataTypeInt;
    }

    List<String> getInputFileNames() {
        return inputFileNames;
    }

    String getOutputFileName() {
        return outputFileName;
    }
}
