package p1;

import java.io.*;
/* import p1.CsvNormalizer; */

/**
 * Normalizes input CSV provided on stdin and writes to output CSV on stdout
 */
public class Normalizer {
    public static void main(String[] args) throws IOException {

        // Get Reader and Writer from stdin and stout
        Reader inputReader = new InputStreamReader(System.in);
        BufferedWriter outputWriter = new BufferedWriter(new OutputStreamWriter(System.out));

        // Init a CSV Normalizer and run normalization
        CsvNormalizer csvNormalizer = new CsvNormalizer();
        try {
            csvNormalizer.normalizeCsv(inputReader, outputWriter);

            // Close Reader/Writer
            inputReader.close();
            outputWriter.close();
        } catch (IOException ex) {
            // Print to stderr if unable to read CSV from stdin
            System.err.println("Unable to read file from stdin. Exception: " + ex.getMessage());
        }
    }
}
