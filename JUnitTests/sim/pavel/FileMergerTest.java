package sim.pavel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileMergerTest {
    Properties properties;
    FileMerger fileMerger;

    @BeforeEach
    public void initEach() {
        Properties properties = Properties.fromArgs(new String[] {"-a", "-i", "JUNITout.txt", "JUNITinput1.txt"});
        FileMerger fileMerger = new FileMerger(properties);
        fileMerger.writeMergedFiles(properties.getOutputFileName());
        this.properties = properties;
        this.fileMerger = fileMerger;
    }

    @Test
    void writeMergedFiles() {
    }

    @Disabled
    void getScanners() {
    }
}