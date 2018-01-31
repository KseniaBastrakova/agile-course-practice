package ru.unn.agile.RatioCalculator.infrastructure_lab3;

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
        testTxtLogger = new TxtLogger(FILE_NAME);
    }

    @Test
    public void canCreateLoggerWithFileName() {
        assertNotNull(testTxtLogger);
    }

    @Test
    public void canCreateLogFileOnDisk() {
        try {
            new BufferedReader(new FileReader(FILE_NAME));
        } catch (FileNotFoundException exception) {
            fail("File " + FILE_NAME + " wasn't found!");
        }
    }

    @Test
    public void canWriteLogMessage() {
        String testMessage = "Test message";

        testTxtLogger.log(testMessage);

        String message = testTxtLogger.get().get(0);
        assertTrue(message.matches(".*" + testMessage + "$"));
    }

    @Test
    public void canWriteSeveralLogMessage() {
        String[] messages = {"Message 1", "Message 2"};

        testTxtLogger.log(messages[0]);
        testTxtLogger.log(messages[1]);

        List<String> actualMessages = testTxtLogger.get();
        for (int i = 0; i < actualMessages.size(); ++i) {
            assertTrue(actualMessages.get(i).matches(".*" + messages[i] + "$"));
        }
    }

    @Test
    public void doesLogContainDateAndTime() {
        String testMessage = "Test message";

        testTxtLogger.log(testMessage);

        String message = testTxtLogger.get().get(0);
        assertTrue(message.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2} > .*"));
    }

    private static final String FILE_NAME = "./TxtLogger_Tests-lab3.log";
    private TxtLogger testTxtLogger;
}
