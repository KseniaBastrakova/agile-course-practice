package ru.unn.agile.RatioCalculator.Infrastructure;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

public class TxtLoggerTests {
    @Before
    public void setUp() {
        testLogger = new TxtLogger(NAME_LOG);
    }

    @Test
    public void canCreateLoggerWithFileName() {
        assertNotNull(testLogger);
    }

    @Test
    public void doesLogContainsDateAndTime() {
        String test = "Test message with time";

        testLogger.log(test);

        String message = testLogger.get().get(0);
        assertTrue(message.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*"));
    }

    @Test
    public void canCreateFile() {
        try {
            new BufferedReader(new FileReader(NAME_LOG));
        } catch (FileNotFoundException ex) {
            fail("File " + NAME_LOG + " not found!");
        }
    }

    @Test
    public void canWriteLogMessage() {
        String testMessage = "Test message";

        testLogger.log(testMessage);

        String message = testLogger.get().get(0);
        assertTrue(message.matches(".*" + testMessage + "$"));
    }

    @Test
    public void canAddMultiplyLogMessage() {
        String[] messages = {"input 1", "input 2"};

        testLogger.log(messages[0]);
        testLogger.log(messages[1]);

        List<String> actualMessages = testLogger.get();
        for (int i = 0; i < actualMessages.size(); ++i) {
            assertTrue(actualMessages.get(i).matches(".*" + messages[i] + "$"));
        }
    }

    private static final String NAME_LOG = "./TestLog.log";
    private TxtLogger testLogger;
}
