package sim.pavel;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static sim.pavel.MyLogger.log;

public class FileMergerForBufferedReader { //Класс отвечает за слияние заранее отсортированных файлов в один большой отсортированный файл

    private ArrayList<FileContentBufferedReader> scanners = new ArrayList<>(); //список сканнеров (каждый сканнер отвечает за отдельный входной файл)
    private final boolean descendingSortOrder;
    private final List<String> inputFileNames;

    public FileMergerForBufferedReader(Properties properties) {
        this.descendingSortOrder = properties.isDescendingSortOrder();
        this.inputFileNames = properties.getInputFileNames();
    }

    void writeMergedFiles(String outputFileName) {
        try { //создаем writer для выходного файла
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFileName), StandardCharsets.UTF_8));
            mergeData(writer);
            writer.close();
        } catch(Exception e) {
            log().warning(e.getMessage());
            e.printStackTrace();
        }
    }

    //возвращаем сканнер с наименьшим/наибольшим значением (в зависимости от метода сортировки)
    //в зависимости от состояния descendingSortOrder делаем соответствующую сортировку
    private FileContentBufferedReader getScannerWithHighestOrLowestValue() {
        if (scanners.size() == 0) return null;

        FileContentBufferedReader scannerWithHighestOrLowestValue = scanners.get(0);
        for (FileContentBufferedReader scanner : scanners) {
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
                    scanners.add(new FileContentBufferedReader((fileContentScannerPath)));
                } catch (Exception e) {
                    e.printStackTrace();
                    log().severe(e.getMessage());
                }
            } else {
                log().warning("Error opening input file : \"" + fileContentScannerPath.getName() + "\" : file is empty/doesn't exist");
            }
        }
    }

    private void removeFromScannersList(FileContentBufferedReader scannerToRemove) {
        scanners.remove(scannerToRemove);
    }

    private void mergeData(Writer writer) throws IOException {
        makeListOfInputFileScanners(inputFileNames);
        FileContentBufferedReader scannerWithGoalValue = getScannerWithHighestOrLowestValue();
        //previouslyAddedScanner заводим для отслеживания последнего добавленного в выходной файл значения
        //это нужно для того, чтобы отследить ситуацию, когда один из входных файлов окажется не отсортированным
        FileContentBufferedReader scannerWithPreviouslyWrittenValue = new FileContentBufferedReader();
        scannerWithPreviouslyWrittenValue.setValue(Objects.requireNonNull(getScannerWithHighestOrLowestValue()).getValue());
        //идём по всем сканнерам, записывая из них самое высокое/низкое значение, пока сканнеры не кончатся
        while(scannerWithGoalValue != null) {
            if((scannerWithGoalValue.hasNext())) {
                if (isInputValueIncorrect(scannerWithGoalValue, scannerWithPreviouslyWrittenValue)) {
                    skipScannerValueAndLogIt(scannerWithGoalValue);
                } else {
                    scannerWithPreviouslyWrittenValue.setValue(scannerWithGoalValue.getValue().trim());
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
    private boolean isInputValueIncorrect(FileContentBufferedReader scannerWithGoalValue, FileContentBufferedReader previouslyAddedScanner) {
        return scannerWithGoalValue.getValue().trim().matches("^(.*)(\\s+)(.*)$") || (isInputFileUnsorted(descendingSortOrder, previouslyAddedScanner, scannerWithGoalValue));
    }

    //Не записываем значение сканнера, выводим информацию в лог и сканируем следующее значение.
    private void skipScannerValueAndLogIt(FileContentBufferedReader scannerWithHighestOrLowestValue) {
        log().log(Level.WARNING, "Element \"" + scannerWithHighestOrLowestValue.getValue().trim() + "\" from file " +
                scannerWithHighestOrLowestValue.source + " has been skipped because Value was invalid");
        scannerWithHighestOrLowestValue.getValueAndScanNext();
    }

    //нарушен ли порядок сортировки во входном файле
    private boolean isInputFileUnsorted(boolean descendingSortOrder, FileContentBufferedReader previouslyAddedScanner, FileContentBufferedReader scannerWithGoalValue) {
        if (descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) < 0) {
            return true;
        }
        return !descendingSortOrder && previouslyAddedScanner.compareTo(scannerWithGoalValue) > 0;
    }
}
