package sim.pavel;

import java.io.*;
import java.math.BigInteger;

import static sim.pavel.MyLogger.log;

//Данный класс предназначен для использования сканнеров (по одному на каждый входной файл).
public class FileContentScanner {
    private BufferedReader bufferedReader;

    private String currentValue;
    String source;

    public FileContentScanner(File source, boolean descendingSortOrder) throws Exception {
        this.source = source.toString();
        if (descendingSortOrder) {
            this.bufferedReader = new BufferedReader(new InputStreamReader(new ReverseInputStream(source)));
        } else {
            this.bufferedReader = new BufferedReader(new FileReader(source));
        }
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
            if (currentValue != null) currentValue = currentValue.trim();
        } catch (IOException e) {
            log().warning(e.getMessage());
        }
    }

    boolean hasNext() {
        return (currentValue != null);
    }

    String getValueAndReadNext() {
        String scannedValue = currentValue;
        scanNextValue();

        return scannedValue;
    }

    //можно использовать для сравнения значений Value сканнеров, причем Value может быть как числом так и String
    int compareTo(FileContentScanner other) {
        String thisValue = this.getValue();
        String otherValue = other.getValue();
        if (thisValue.matches("\\d+") && otherValue.matches("\\d+")) {
            BigInteger bigIntegerThisValue = new BigInteger(thisValue);
            BigInteger bigIntegerOtherValue = new BigInteger(otherValue);
            return bigIntegerThisValue.compareTo(bigIntegerOtherValue);
        } else {
            return thisValue.compareTo(otherValue);
        }
    }
}
