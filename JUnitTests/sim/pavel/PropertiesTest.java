package sim.pavel;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesTest {
    @ParameterizedTest
    @CsvSource({
            "-d, -i, test\\out.txt, test\\in1.txt, test\\in3.txt",
            "-d, -s, test\\out.txt, test\\in1.txt, test\\in3.txt",
    })
    void testFromArgsDescending(String a, String b, String c, String d, String e) {
        Properties properties = Properties.fromArgs(new String[] {a, b, c, d, e});
        assertTrue(properties.isDescendingSortOrder());
    }

    @ParameterizedTest
    @CsvSource({
            "-a, -i, test\\out.txt, test\\in1.txt, test\\in3.txt",
            "-a, -s, test\\out.txt, test\\in1.txt, test\\in3.txt",
            "-i, test\\out.txt, test\\in1.txt, test\\in3.txt, test\\in3.txt",
            "-s, test\\out.txt, test\\in1.txt, test\\in3.txt, test\\in3.txt",
    })
    void testFromArgsAscending(String a, String b, String c, String d, String e) {
        Properties properties = Properties.fromArgs(new String[] {a, b, c, d, e});
        assertFalse(properties.isDescendingSortOrder());
    }

    @Test
    void isDescendingSortOrder() {
    }

    @Test
    void isDataTypeIsInt() {
    }

    @Test
    void getInputFileNames() {
    }

    @Test
    void getOutputFileName() {
    }

}