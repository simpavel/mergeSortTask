package sim.pavel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class FileContentScannerTest {
    Properties properties;
    FileMerger fileMerger;
    FileContentScanner fileContentScanner;

    @BeforeEach
        public void initEach() {
        Properties properties = Properties.fromArgs(new String[] {"-a", "-i", "JUNITout.txt", "JUNITinput1.txt"});
        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName());
        this.properties = properties;
        this.fileMerger = fileMerger;
        FileContentScanner fileContentScanner = new FileContentScanner();
        fileContentScanner.setValue("firstScannerValue");
        this.fileContentScanner = fileContentScanner;
    }

    @Test
    void getValue() {
        assertEquals("firstScannerValue", fileContentScanner.getValue());
    }

    @Test
    void setValue() {
        fileContentScanner.setValue(null);
        assertNull(fileContentScanner.getValue());
    }

    @Disabled
    void scanNextValue() {
    }

    @Disabled
    void hasNext() {
    }

    @Disabled
    void getValueAndScanNext() {
    }

    @Test
    void compareTo() {
        FileContentScanner otherFileContentScanner = new FileContentScanner();
        otherFileContentScanner.setValue("BB");
        assertTrue(fileContentScanner.compareTo(otherFileContentScanner) > 0);
        assertFalse(fileContentScanner.compareTo(otherFileContentScanner) < 0);
        fileContentScanner.setValue("BB");
        assertEquals(0, fileContentScanner.compareTo(otherFileContentScanner));
        fileContentScanner.setValue("24");
        otherFileContentScanner.setValue("12");
        assertTrue(fileContentScanner.compareTo(otherFileContentScanner) > 0);
        assertFalse(fileContentScanner.compareTo(otherFileContentScanner) < 0);
        otherFileContentScanner.setValue("24");
        assertEquals(0, fileContentScanner.compareTo(otherFileContentScanner));
        otherFileContentScanner.setValue("4555555555555555555555555555555555645645");
        assertFalse(fileContentScanner.compareTo(otherFileContentScanner) > 0);
    }
}