package p1;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CsvNormalizerTest {

    /**
     * Test normalizing a simple, valid CSV
     */
    @Test
    public void testNormalizeCsv() {

        // Mock input reader
        String inputCsvString = "Column1, FullName, ZIP\nTest, name1, 1";
        Reader inputReader = new StringReader(inputCsvString);

        // Mock output writer
        Writer writer = new StringWriter();
        BufferedWriter outputWriter = new BufferedWriter(writer);

        // Run normalizeCsv with the mock reader/writer
        CsvNormalizer csvNormalizer = new CsvNormalizer();
        try {
            csvNormalizer.normalizeCsv(inputReader, outputWriter);
        } catch (IOException ex) {
            assertTrue(false);
        }

        // Validate what was written to the writer
        String normalizedCsv = writer.toString();
        assertTrue(normalizedCsv.length() > 0);
        assertTrue(normalizedCsv.contains("Column1,FullName,ZIP"));
        assertTrue(normalizedCsv.contains("Test,NAME1,00001"));

        try {
            inputReader.close();
            outputWriter.close();
            writer.close();
        } catch (IOException ex) {
            assertTrue(false);
        }
    }

    /**
     * Test normalizing a single CSV row
     */
    @Test
    public void testNormalizeCsvRow() {

        // Expected output
        ArrayList<String> expectedNormalizedRow = new ArrayList<String>(Arrays.asList("Test", "NAME1", "00001"));

        // Mock input reader
        String inputCsvString = "Column1, FullName, ZIP\nTest, name1, 1";
        Reader reader = new StringReader(inputCsvString);

        ArrayList<String> normalizedRow = new ArrayList<String>();
        try {
            // Use mock reader to get a CSVRecord
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());
            CSVRecord csvRow = csvParser.getRecords().get(0);
            String[] csvHeaders = csvParser.getHeaderNames().toArray(new String[0]);

            // Normalize the csv row
            CsvNormalizer csvNormalizer = new CsvNormalizer();
            normalizedRow = csvNormalizer.normalizeCsvRow(csvHeaders, csvRow);

            csvParser.close();
            reader.close();
        } catch (Exception ex) {
            assertTrue(false);
        }

        // Test csv row was normalized as expected
        assertTrue(normalizedRow.toString().equals(expectedNormalizedRow.toString()));
    }

    /**
     * Test calculating the total duration from a CSV row
     */
    @Test
    public void testCalculateTotalDuration() {

        // Mock input
        String inputCsvString = "FooDuration, BarDuration\n1:23:45.678, 1:23:32.123";
        Reader reader = new StringReader(inputCsvString);

        String totalDuration = "";
        try {
            // Create CSVRecord from reader
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim());
            CSVRecord csvRow = csvParser.getRecords().get(0);

            // Calculatae the TotalDuration for the CSV row
            CsvNormalizer csvNormalizer = new CsvNormalizer();
            totalDuration = csvNormalizer.calculateTotalDuration(csvRow);

            csvParser.close();
            reader.close();
        } catch (Exception ex) {
            assertTrue(false);
        }

        // Test total duration is the correct number of seconds
        assertTrue(totalDuration.equals("10037.801"));
    }
}
