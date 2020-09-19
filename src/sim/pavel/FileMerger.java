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
    private FileContentScanner getScannerWithHighestOrLowestValue() {
        if (bufferedReaders.size() == 0) return null;

        FileContentScanner scannerWithHighestOrLowestValue = bufferedReaders.get(0);
        for (FileContentScanner scanner : bufferedReaders) {
            if ((scannerWithHighestOrLowestValue.compareTo(scanner) >= 0) && !descendingSortOrder) {
                scannerWithHighestOrLowestValue = scanner;
            }
            if ((scannerWithHighestOrLowestValue.compareTo(scanner) <= 0) && descendingSortOrder) {
                scannerWithHighestOrLowestValue = scanner;

            }
        }
        return scannerWithHighestOrLowestValue;
    }

    private void makeListOfInputFileScanners(List<String> inputFileNames) {
        for (String filename : inputFileNames) {
            File fileContentScannerPath = new File(filename);
            //Проверка на ненулевой входной файл
            if (fileContentScannerPath.length() > 0) {
                try {
                    bufferedReaders.add(new FileContentScanner((fileContentScannerPath), dataTypeInt, descendingSortOrder));
                } catch (Exception e) {
                    e.printStackTrace();
                    log().severe(e.getMessage());
                }
            } else {
                log().warning("Error opening input file : \"" + fileContentScannerPath.getName() +
                        "\" : file is empty/doesn't exist");
            }
        }
    }

    private void removeFromScannersList(FileContentScanner scannerToRemove) {
        bufferedReaders.remove(scannerToRemove);
    }

    private void mergeData(Writer writer) throws IOException {
        makeListOfInputFileScanners(inputFileNames);
        FileContentScanner scannerWithGoalValue = getScannerWithHighestOrLowestValue();
        //previouslyAddedScanner заводим для отслеживания последнего добавленного в выходной файл значения
        //это нужно для того, чтобы отследить ситуацию, когда один из входных файлов окажется не отсортированным
        FileContentScanner scannerWithPreviouslyWrittenValue = new FileContentScanner();
        scannerWithPreviouslyWrittenValue.setValue(Objects.requireNonNull
                (getScannerWithHighestOrLowestValue()).getValue());
        //идём по всем сканнерам, записывая из них самое высокое/низкое значение, пока сканнеры не кончатся
        while(scannerWithGoalValue != null) {
            if((scannerWithGoalValue.hasNext())) {
                if (isInputValueIncorrect(scannerWithGoalValue, scannerWithPreviouslyWrittenValue)) {
                    skipScannerValueAndLogIt(scannerWithGoalValue);
                } else {
                    scannerWithPreviouslyWrittenValue.setValue(scannerWithGoalValue.getValue());
                    writer.write(scannerWithGoalValue.getValueAndScanNext() + "\n");
                }
                if (scannerWithGoalValue.hasNext()) {
                    scannerWithGoalValue = getScannerWithHighestOrLowestValue();
                }
            } else {
                removeFromScannersList(scannerWithGoalValue);
                scannerWithGoalValue = getScannerWithHighestOrLowestValue();
            }
        }
    }

    //Если считанное значение либо из неотсортированного списка, либо имеет пробелы (пункт задания про пробелы)
    //либо программа, запущенная с аргументом -i(для сортировки чисел) получает из входного файла строки
    private boolean isInputValueIncorrect(FileContentScanner scannerWithGoalValue,
                                          FileContentScanner previouslyAddedScanner) {
        return scannerWithGoalValue.getValue().matches("^(.*)(\\s+)(.*)$") ||
                (isInputFileUnsorted(descendingSortOrder, previouslyAddedScanner, scannerWithGoalValue)) ||
                ((scannerWithGoalValue.getValue().matches("\\D+")) && (dataTypeInt));
    }

    //Не записываем значение сканнера, выводим информацию в лог и сканируем следующее значение.
    private void skipScannerValueAndLogIt(FileContentScanner scannerWithHighestOrLowestValue) {
        log().log(Level.WARNING, "Element \"" + scannerWithHighestOrLowestValue.getValue() + "\" from file " +
                scannerWithHighestOrLowestValue.source + " has been skipped because Value was invalid");
        scannerWithHighestOrLowestValue.getValueAndScanNext();
    }

    //нарушен ли порядок сортировки во входном файле
    private boolean isInputFileUnsorted(boolean descendingSortOrder, FileContentScanner previouslyAddedScanner,
                                        FileContentScanner scannerWithGoalValue) {
        if (descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) < 0) {
            return true;
        }
        return !descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) > 0;
    }
}
