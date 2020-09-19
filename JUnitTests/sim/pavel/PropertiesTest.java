package sim.pavel;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.opentest4j.AssertionFailedError;

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
    void testIfDescendingSortOrder() {
        Properties properties = Properties.fromArgs(new String[] {"-i", "something", "something"});
        assertFalse(properties.isDescendingSortOrder());

        properties = Properties.fromArgs(new String[] {"-a", "something", "something"});
        assertFalse(properties.isDescendingSortOrder());

        properties = Properties.fromArgs(new String[] {"-d", "something", "something"});
        assertTrue(properties.isDescendingSortOrder());
    }

    @Test
    void isDataTypeInt() {
        Properties properties = Properties.fromArgs(new String[] {"-i", "something", "something"});
        assertTrue(properties.isDataTypeInt());

        properties = Properties.fromArgs(new String[] {"-s", "something", "something"});
        assertFalse(properties.isDataTypeInt());

        properties = Properties.fromArgs(new String[] {"-a", "-i", "something"});
        assertTrue(properties.isDataTypeInt());

        properties = Properties.fromArgs(new String[] {"-d", "-s", "something"});
        assertFalse(properties.isDataTypeInt());
    }

    @ParameterizedTest
    @CsvSource({
            "-a, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-a, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-d, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-d, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
    })
    void getInputFileNamesWithFirstArgument(String a, String b, String c, String d, String e) {
        Properties properties = Properties.fromArgs(new String[] {a, b, c, d, e});
        assertEquals(2, properties.getInputFileNames().size());
        }

    @ParameterizedTest
    @CsvSource({
            "-i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-s, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-s, test\\out.txt, test\\in1.txt, test\\in2.txt",
    })
    void getInputFileNamesWithoutFirstArgument(String a, String b, String c, String d) {
        Properties properties = Properties.fromArgs(new String[] {a, b, c, d});
        assertEquals(2, properties.getInputFileNames().size());
        }

    @ParameterizedTest
    @CsvSource({
            "-a, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-a, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-d, -i, test\\out.txt, test\\in1.txt, test\\in2.txt",
            "-d, -s, test\\out.txt, test\\in1.txt, test\\in2.txt",
    })
    void getOutputFileName(String a, String b, String c, String d, String e) {
        Properties properties = Properties.fromArgs(new String[] {a, b, c, d, e});
        assertEquals("test\\out.txt", properties.getOutputFileName());
    }

}