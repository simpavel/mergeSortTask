package sim.pavel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static sim.pavel.MyLogger.log;

//Класс отвечает за слияние заранее отсортированных файлов в один большой отсортированный файл
public class FileMerger {

    //список сканнеров (каждый сканнер отвечает за отдельный входной файл)
    private ArrayList<FileContentScanner> bufferedReaders = new ArrayList<>();
    private final boolean descendingSortOrder;
    private final List<String> inputFileNames;
    private boolean dataTypeInt;

    public FileMerger(Properties properties) {
        this.descendingSortOrder = properties.isDescendingSortOrder();
        this.inputFileNames = properties.getInputFileNames();
        this.dataTypeInt = properties.isDataTypeInt();
    }

    void writeMergedFiles(String outputFileName) {
        try {
            //создаем writer для выходного файла
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName),
                    StandardCharsets.UTF_8));
            mergeData(writer);
            writer.close();
        } catch(Exception e) {
            log().warning(e.getMessage());
            e.printStackTrace();
        }
    }

    //возвращаем сканнер с наименьшим/наибольшим значением (в зависимости от метода сортировки)
    //в зависимости от состояния descendingSortOrder делаем соответствующую сортировку
    private FileContentScanner getReaderWithHighestOrLowestValue() {
        if (bufferedReaders.size() == 0) return null;

        FileContentScanner readerWithHighestOrLowestValue = bufferedReaders.get(0);
        for (FileContentScanner reader : bufferedReaders) {
            if ((readerWithHighestOrLowestValue.compareTo(reader) >= 0) && !descendingSortOrder) {
                readerWithHighestOrLowestValue = reader;
            }
            if ((readerWithHighestOrLowestValue.compareTo(reader) <= 0) && descendingSortOrder) {
                readerWithHighestOrLowestValue = reader;

            }
        }
        return readerWithHighestOrLowestValue;
    }

    private void makeAListOfReaders(List<String> inputFileNames) {
        for (String filename : inputFileNames) {
            File sourceFileName = new File(filename);
            //Проверка на ненулевой входной файл
            if (sourceFileName.length() > 0) {
                try {
                    bufferedReaders.add(new FileContentScanner((sourceFileName), descendingSortOrder));
                } catch (Exception e) {
                    e.printStackTrace();
                    log().severe(e.getMessage());
                }
            } else {
                log().warning("Error opening input file : \"" + sourceFileName.getName() +
                        "\" : file is empty/doesn't exist");
            }
        }
    }

    private void removeFromReaderLists(FileContentScanner readerToRemove) {
        bufferedReaders.remove(readerToRemove);
    }

    private void mergeData(Writer writer) throws IOException {
        makeAListOfReaders(inputFileNames);
        FileContentScanner readerWithGoalValue = getReaderWithHighestOrLowestValue();
        //previouslyAddedScanner заводим для отслеживания последнего добавленного в выходной файл значения
        //это нужно для того, чтобы отследить ситуацию, когда один из входных файлов окажется не отсортированным
        FileContentScanner readerWithPreviouslyWrittenValue = new FileContentScanner();
        readerWithPreviouslyWrittenValue.setValue(Objects.requireNonNull
                (getReaderWithHighestOrLowestValue()).getValue());
        //идём по всем сканнерам, записывая из них самое высокое/низкое значение, пока сканнеры не кончатся
        while(readerWithGoalValue != null) {
            if((readerWithGoalValue.hasNext())) {
                if (isInputValueIncorrect(readerWithGoalValue, readerWithPreviouslyWrittenValue)) {
                    skipReaderValueAndLogIt(readerWithGoalValue);
                } else {
                    readerWithPreviouslyWrittenValue.setValue(readerWithGoalValue.getValue());
                    writer.write(readerWithGoalValue.getValueAndReadNext() + "\n");
                }
                if (readerWithGoalValue.hasNext()) {
                    readerWithGoalValue = getReaderWithHighestOrLowestValue();
                }
            } else {
                removeFromReaderLists(readerWithGoalValue);
                readerWithGoalValue = getReaderWithHighestOrLowestValue();
            }
        }
    }

    //Если считанное значение либо из неотсортированного списка, либо имеет пробелы (пункт задания про пробелы)
    //либо программа, запущенная с аргументом -i(для сортировки чисел) получает из входного файла строки
    private boolean isInputValueIncorrect(FileContentScanner readerWithGoalValue,
                                          FileContentScanner previouslyAddedReader) {
        return readerWithGoalValue.getValue().matches("^(.*)(\\s+)(.*)$") ||
                (isInputFileUnsorted(descendingSortOrder, previouslyAddedReader, readerWithGoalValue)) ||
                ((readerWithGoalValue.getValue().matches("\\D+")) && (dataTypeInt));
    }

    //Не записываем значение сканнера, выводим информацию в лог и сканируем следующее значение.
    private void skipReaderValueAndLogIt(FileContentScanner readerWithHighestOrLowestValue) {
        log().log(Level.WARNING, "Element \"" + readerWithHighestOrLowestValue.getValue() + "\" from file " +
                readerWithHighestOrLowestValue.source + " has been skipped because Value was invalid");
        readerWithHighestOrLowestValue.getValueAndReadNext();
    }

    //нарушен ли порядок сортировки во входном файле
    private boolean isInputFileUnsorted(boolean descendingSortOrder, FileContentScanner previouslyAddedReader,
                                        FileContentScanner readerWithGoalValue) {
        if (descendingSortOrder && previouslyAddedReader.compareTo(readerWithGoalValue) < 0) {
            return true;
        }
        return !descendingSortOrder && previouslyAddedReader.compareTo(readerWithGoalValue) > 0;
    }
}
