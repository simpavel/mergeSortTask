package sim.pavel;

import java.io.*;
import java.math.BigInteger;
import java.util.Scanner;
//Данный класс предназначен для использования сканнеров (по одному на каждый входной файл).
public class FileContentBufferedReader {
    private BufferedReader bufferedReader;

    private String currentValue;
    public String source;

    public FileContentBufferedReader(File source) throws Exception {
        this.source = source.toString();

        bufferedReader = new BufferedReader(new FileReader(source));

        scanNextValue();
    }

    //пустой конструктор здесь для того, чтобы я мог создать "previouslyAddedScanner" в
    //методе merge класса FileMerger

    public FileContentBufferedReader() {
    }

    public String getValue() {
        return currentValue;
    }

    public void setValue(String currentValue) {
        this.currentValue = currentValue;
    }

    public void scanNextValue() {

        try {
            currentValue = (bufferedReader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public int compareTo(FileContentBufferedReader other) {
        if (this.getValue().trim().matches("\\d+") && other.getValue().trim().matches("\\d+")) {
            BigInteger bigIntegerThisValue = new BigInteger(getValue().trim());
            BigInteger bigIntegerOtherValue = new BigInteger(other.getValue().trim());
            return bigIntegerThisValue.compareTo(bigIntegerOtherValue);
        } else {
            return getValue().trim().compareTo(other.getValue().trim());
        }
    }
}
