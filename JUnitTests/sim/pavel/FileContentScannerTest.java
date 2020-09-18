package sim.pavel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FileContentScannerTest {

    @BeforeEach
        public void initEach() {
        Properties properties = Properties.fromArgs(new String[] {"-a", "-i", "\\JUnitTests\\out.txt", "\\JUnitTests\\input1.txt"});
        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName());
    }

    @Test
    void getValue() {


    }

    @Test
    void setValue() {
    }

    @Test
    void scanNextValue() {
    }

    @Test
    void hasNext() {
    }

    @Test
    void getValueAndScanNext() {
    }

    @Test
    void compareTo() {
    }
}