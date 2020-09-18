package sim.pavel;

import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;
//Данный класс предназначен для использования сканнеров (по одному на каждый входной файл).
public class FileContentScanner {
    private Scanner scanner;
    private String currentValue;
    public String source;

    public FileContentScanner(File source) throws Exception {
        this.source = source.toString();
        scanner = new Scanner(source);
        scanner.useDelimiter("\\n");
        scanNextValue();
    }

    //пустой конструктор здесь для того, чтобы я мог создать "previouslyAddedScanner" в
    //методе merge класса FileMerger
    public FileContentScanner() {
    }

    public String getValue() {
        return currentValue;
    }

    public void setValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void scanNextValue() {
        currentValue = (scanner.hasNext() ? scanner.next() : null);
    }

    public boolean hasNext() {
        return (currentValue != null);
    }

    public String getValueAndScanNext() {
        String scannedValue = currentValue;
        scanNextValue();
        return scannedValue;
    }

    //можно использовать для сравнения значений Value сканнеров, причем Value может быть как числом так и String
    public int compareTo(FileContentScanner other) {
        if (this.getValue().trim().matches("\\d+") && other.getValue().trim().matches("\\d+")) {
            BigInteger bigIntegerThisValue = new BigInteger(getValue().trim());
            BigInteger bigIntegerOtherValue = new BigInteger(other.getValue().trim());
            return bigIntegerThisValue.compareTo(bigIntegerOtherValue);
        } else {
            return getValue().trim().compareTo(other.getValue().trim());
        }
    }
}
