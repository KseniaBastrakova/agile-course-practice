package ru.unn.agile.RatioCalculator.Infrastructure;

import ru.unn.agile.RatioCalculator.viewmodel.ILogger;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TxtLogger implements ILogger {
    public TxtLogger(final String folderName) {

        BufferedWriter currentLog = null;
        this.folderName = folderName;
        try {
            currentLog = new BufferedWriter(new FileWriter(folderName));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        writer = currentLog;
    }
    @Override
    public List<String> get() {
        BufferedReader bufferedReader;
        ArrayList<String> currentLog = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(folderName));
            String message = bufferedReader.readLine();

            while (message != null) {
                currentLog.add(message);
                message = bufferedReader.readLine();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return currentLog;
    }

    @Override
    public void log(final String input) {
        try {
            writer.write(now() + " > " + input);
            writer.newLine();
            writer.flush();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }


    private static String now() {
        Calendar date = Calendar.getInstance();
        SimpleDateFormat curreent = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        return curreent.format(date.getTime());
    }

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final BufferedWriter writer;
    private final String folderName;
}
