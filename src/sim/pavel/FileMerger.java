package sim.pavel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static sim.pavel.MyLogger.log;

public class FileMerger { //Класс отвечает за слияние заранее отсортированных файлов в один большой отсортированный файл
    private ArrayList<FileContentScanner> scanners = new ArrayList<>(); //список сканнеров (каждый сканнер отвечает за отдельный входной файл)
    private final boolean descendingSortOrder;
    private final List<String> inputFileNames;

    public FileMerger(Properties properties) {
        this.descendingSortOrder = properties.isDescendingSortOrder();
        this.inputFileNames = properties.getInputFileNames();
    }

    public void writeMergedFiles(String outputFileName) {
        try { //создаем writer для выходного файла
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8));
            mergeData(writer);
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    //возвращаем сканнер с наименьшим/наибольшим значением (в зависимости от метода сортировки)
    //в зависимости от состояния descendingSortOrder делаем соответствующую сортировку
    private FileContentScanner getScannerWithHighestOrLowestValue() {
        if (scanners.size() == 0) return null;

        FileContentScanner scannerWithHighestOrLowestValue = scanners.get(0);
        for (FileContentScanner scanner : scanners) {
            if ((scannerWithHighestOrLowestValue.compareTo(scanner) >= 0) && !descendingSortOrder) {
                scannerWithHighestOrLowestValue = scanner;
            }
            if ((scannerWithHighestOrLowestValue.compareTo(scanner) < 0) && descendingSortOrder) {
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
                    scanners.add(new FileContentScanner((fileContentScannerPath)));
                } catch (Exception e) {
                    e.printStackTrace();
                    log().severe(e.getMessage());
                }
            } else {
                log().warning("Error opening input file : \"" + fileContentScannerPath.getName() + "\" : file is empty/doesn't exist");
            }
        }
    }

    private void removeFromScannersList(FileContentScanner scannerToRemove) {
        scanners.remove(scannerToRemove);
    }

    private void mergeData(Writer writer) throws IOException {
        makeListOfInputFileScanners(inputFileNames);
        //сканнер с наибольшим/наименьшим значением:
        FileContentScanner scannerWithGoalValue = getScannerWithHighestOrLowestValue();
        //previouslyAddedScanner заводим для отслеживания последнего добавленного в выходной файл значения
        //это нужно для того, чтобы отследить ситуацию, когда один из входных файлов окажется не отсортированным
        FileContentScanner scannerWithPreviouslyWrittenValue = new FileContentScanner();
        scannerWithPreviouslyWrittenValue.setValue(Objects.requireNonNull(getScannerWithHighestOrLowestValue()).getValue());
        //идём по всем сканнерам, записывая из них самое высокое/низкое значение, пока сканнеры не кончатся
        while(scannerWithGoalValue != null) {
            if((scannerWithGoalValue.hasNext())) {
                if (isInputValueIncorrect(scannerWithGoalValue, scannerWithPreviouslyWrittenValue)) {
                    skipScannerValueAndLogIt(scannerWithGoalValue);
                } else {
                    scannerWithPreviouslyWrittenValue.setValue(scannerWithGoalValue.getValue().trim());
                    //TODO здесь эта \n даёт в конце файла лишний newline. Мб убрать это
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
    private boolean isInputValueIncorrect(FileContentScanner scannerWithGoalValue, FileContentScanner previouslyAddedScanner) {
        return scannerWithGoalValue.getValue().matches("(\\s+)") || (isInputFileUnsorted(descendingSortOrder, previouslyAddedScanner, scannerWithGoalValue));
    }

    //Не записываем значение сканнера, выводим информацию в лог и сканируем следующее значение.
    private void skipScannerValueAndLogIt(FileContentScanner scannerWithHighestOrLowestValue) {
        log().log(Level.WARNING, "Element \"" + scannerWithHighestOrLowestValue.getValue().trim() + "\" from file " +
                scannerWithHighestOrLowestValue.source + " has been skipped because Value was invalid");
        MyLogger.incrementExcludedStringsWithSpacesCount();
        scannerWithHighestOrLowestValue.getValueAndScanNext();
    }

    //нарушен ли порядок сортировки во входном файле
    private boolean isInputFileUnsorted(boolean descendingSortOrder, FileContentScanner previouslyAddedScanner, FileContentScanner scannerWithGoalValue) {
        if (descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) < 0) {
            return true;
        }
        return !descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) > 0;
    }
}
