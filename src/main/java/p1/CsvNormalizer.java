package p1;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

/**
 * This class is used to process CSV files and normalize the contained data
 * fields
 */
public class CsvNormalizer {

    /**
     * Normalize the CSV read from the inputReader to the outputWriter. Rows with
     * unparsable values are dropped and the related errors are logged to stderr.
     * 
     * @param inputReader  - Reader pointed to the input CSV
     * @param outputWriter - Writer to print output CSV
     * @throws IOException
     */
    public void normalizeCsv(Reader inputReader, BufferedWriter outputWriter) throws IOException {
        // Initilize CSV Parser
        CSVParser csvParser = new CSVParser(inputReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        // Get column header values and use to initilize CSV Printer
        String[] csvHeaders = csvParser.getHeaderNames().toArray(new String[0]);
        CSVPrinter csvPrinter = new CSVPrinter(outputWriter, CSVFormat.DEFAULT.withHeader(csvHeaders));

        // Normalize and print each CSV row
        for (CSVRecord csvRow : csvParser) {
            try {
                ArrayList<String> normalizedRecord = normalizeCsvRow(csvHeaders, csvRow);
                csvPrinter.printRecord(normalizedRecord);
            } catch (ParseException | NumberFormatException ex) {
                System.err.println("Error Normalizing Row: " + ex);
            }
        }
        csvPrinter.flush();

        // Clean up
        csvParser.close();
        csvPrinter.close();
    }

    /**
     * Normalize all records in the given CSV Row
     * 
     * @param csvHeaders - Array of the CSV column header values
     * @param csvRow     - CSVRecord to be normalized
     * @return - An ArrayList<String> containing all normalized row values
     * @throws ParseException
     * @throws NumberFormatException
     */
    public ArrayList<String> normalizeCsvRow(String[] csvHeaders, CSVRecord csvRow)
            throws ParseException, NumberFormatException {

        ValueNormalizer valueNormalizer = new ValueNormalizer();
        ArrayList<String> normalizedValues = new ArrayList<String>();

        // Normalize each value in the row
        for (String columnName : csvHeaders) {
            // Get column value
            String value = csvRow.get(columnName);

            // Normalize value and add to list
            String normalizedValue;
            if (columnName.equals("TotalDuration")) {
                normalizedValue = calculateTotalDuration(csvRow);
            } else {
                normalizedValue = valueNormalizer.normalizeValue(columnName, value);
            }

            normalizedValues.add(normalizedValue);
        }

        return normalizedValues;
    }

    /**
     * Calculates the total duration from the two expected duration fields in a
     * given CSV row. Total duration is calculated as the sum of the 'FooDuration'
     * column value and the 'BarDuration' column value in seconds.
     * 
     * @param csvRow - CSV row of which to calculate the total duration
     * @return The total duration of both FooDuration and BarDuration as a String
     */
    public String calculateTotalDuration(CSVRecord csvRow) throws NumberFormatException {

        ValueNormalizer valueNormalizer = new ValueNormalizer();

        // Get durations in seconds
        String fooDuration = valueNormalizer.normalizeDuration(csvRow.get("FooDuration"));
        String barDuration = valueNormalizer.normalizeDuration(csvRow.get("BarDuration"));

        // Add durations together
        BigDecimal totalDuration = new BigDecimal(fooDuration).add(new BigDecimal(barDuration));

        return totalDuration.toString();
    }

}