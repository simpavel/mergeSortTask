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

    public static Properties fromArgs(String[] args) {
        //Устанавливаем descendingOrder в соответствии с полученным параметром
        boolean descendingSortOrder = false;
        try {
            descendingSortOrder = args[0].equals("-d");
        } catch (ArrayIndexOutOfBoundsException e) {
            log().severe(e.getMessage() + " args[] are empty");
        }

        //Смотрим, содержит ли первый аргумент -a или -d:
        // (т.к. это не обязательный параметр) если этого параметра не было указано, то следующий параметр будет в индексе args[0]
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
        }

        List<String> inputFileNames = new ArrayList<>();
        //все остальные аргументы - пути к входным файлам
        for (; argumentIndex < args.length; argumentIndex++) {
            inputFileNames.add(args[argumentIndex]);
        }

        return new Properties(descendingSortOrder, dataTypeIsInt, inputFileNames, outputFileName);
    }

    public boolean isDescendingSortOrder() {
        return descendingSortOrder;
    }

    public boolean isDataTypeInt() {
        return dataTypeInt;
    }

    public List<String> getInputFileNames() {
        return inputFileNames;
    }

    public String getOutputFileName() {
        return outputFileName;
    }
}
