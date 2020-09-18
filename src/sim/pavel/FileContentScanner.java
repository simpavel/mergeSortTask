package sim.pavel;

import java.io.*;
import java.math.BigInteger;

//Данный класс предназначен для использования сканнеров (по одному на каждый входной файл).
public class FileContentScanner {
    private BufferedReader bufferedReader;

    private boolean isDataTypeInt;

    private String currentValue;
    String source;

    public FileContentScanner(File source, boolean isDataTypeInt) throws Exception {
        this.source = source.toString();
        this.
        bufferedReader = new BufferedReader(new FileReader(source));
        this.isDataTypeInt = isDataTypeInt;
        scanNextValue();
    }

    //пустой конструктор здесь для того, чтобы я мог создать "previouslyAddedScanner" в
    //методе merge класса FileMerger

    public FileContentScanner() {
    }

    String getValue() {
        return currentValue;
    }

    void setValue(String currentValue) {
        this.currentValue = currentValue;
    }

    private void scanNextValue() {

        try {
            currentValue = (bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean hasNext() {
        return (currentValue != null);
    }

    String getValueAndScanNext() {
        String scannedValue = currentValue;
        scanNextValue();

        return scannedValue;
    }

    //можно использовать для сравнения значений Value сканнеров, причем Value может быть как числом так и String
    int compareTo(FileContentScanner other) {
        if (this.getValue().trim().matches("\\d+") && other.getValue().trim().matches("\\d+")) {
            BigInteger bigIntegerThisValue = new BigInteger(getValue().trim());
            BigInteger bigIntegerOtherValue = new BigInteger(other.getValue().trim());
            return bigIntegerThisValue.compareTo(bigIntegerOtherValue);
        } else {
            return getValue().trim().compareTo(other.getValue().trim());
        }
    }
}
