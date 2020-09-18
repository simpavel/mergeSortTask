package sim.pavel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static sim.pavel.MyLogger.log;

//в этом классе храним данные по входным и выходному файлам (входных может быть более одного)
public class Properties {
    private final boolean descendingSortOrder; //Режим сортировки по умолчанию - возрастающий
    private final boolean dataTypeIsInt; //Вид данных по умолчанию - String (вообще не уверен, зачем мне он нужен, я вроде в FileContentScanner.Compareto сделал так, чтобы было пофиг на вид данных
    private final List<String> inputFileNames; //Список путей к входным файлам
    private final String outputFileName;

    private Properties(boolean descendingSortOrder, boolean dataTypeIsInt,
                       List<String> inputFileNames, String outputFileName) {
        this.descendingSortOrder = descendingSortOrder;
        this.dataTypeIsInt = dataTypeIsInt;
        this.inputFileNames = inputFileNames;
        this.outputFileName = outputFileName;
    }

    public static Properties fromArgs(String[] args) {
        boolean descendingSortOrder = args[0].equals("-d"); //Устанавливаем descendingOrder в соответствии с полученным параметром
        int argumentIndex = args[0].matches("^[-][a|d]$") ? 1 : 0; //Смотрим, содержит ли первый аргумент -a или -d
        // (т.к. это не обязательный параметр) если этого параметра не было указано, то следующий параметр будет в индексе args[0]
        boolean dataTypeIsInt = false;
        String outputFileName = "";

        if (args[argumentIndex].matches("^[-][i|s]$")) {
            dataTypeIsInt = (args[argumentIndex].equals("-i"));
            argumentIndex++;

            outputFileName = args[argumentIndex];
            argumentIndex++;
        } else {
            log().severe("Invalid program arguments detected: " + args[argumentIndex] + " must be -i or -s");
        }

        List<String> inputFileNames = new ArrayList<>();
        for (; argumentIndex < args.length; argumentIndex++) { //все остальные аргументы - пути к входным файлам
            inputFileNames.add(args[argumentIndex]);
        }

        return new Properties(descendingSortOrder, dataTypeIsInt, inputFileNames, outputFileName);
    }

    public boolean isDescendingSortOrder() {
        return descendingSortOrder;
    }

    public boolean isDataTypeIsInt() {
        return dataTypeIsInt;
    }

    public List<String> getInputFileNames() {
        return inputFileNames;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    //Перенёс метод initializeScanners в properties - уберем статик
}
