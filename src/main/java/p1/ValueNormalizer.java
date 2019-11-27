package p1;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Locale;

/**
 * This class contains specific logic to normalize/modify values depending upon
 * the column the value is in. For specific field normalization requerments of
 * the Truss interview problem see https://github.com/trussworks/truss-interview
 */
public class ValueNormalizer {

    /**
     * Normalize a given value in specified way depending on which column the value
     * is from
     * 
     * @param columnName - Name of the column the value came from
     * @param value      - Raw value to be normalized
     * @return Normalized Value
     * @throws ParseException
     * @throws NumberFormatException
     */
    public String normalizeValue(String columnName, String value) throws ParseException, NumberFormatException {

        // Normalize the value in manner determined by which column it is from
        String normalizedValue;
        switch (columnName) {
        case "Timestamp":
            normalizedValue = normalizeTimestamp(value);
            break;
        case "ZIP":
            normalizedValue = normalizeZip(value);
            break;
        case "FullName":
            normalizedValue = normalizeFullName(value);
            break;
        case "FooDuration":
            normalizedValue = normalizeDuration(value);
            break;
        case "BarDuration":
            normalizedValue = normalizeDuration(value);
            break;
        default:
            // Passing through other column values w/out changes. Non-utf8 chars are
            // converted to the Unicode Replacment Character when reading in CSV values.
            normalizedValue = value;
            break;
        }

        return normalizedValue;
    }

    /**
     * Converts the given timestamp from US/Pacific time to US/Eastern time and
     * formats in ISO-8601 format
     * 
     * @param timestamp - Raw input timestamp to be normalized, expects format:
     *                  MM/dd/yy HH:mm:ss a
     * @return Normalized timestamp in US/Eastern time and ISO-8601 format
     * @throws ParseException - Throws when unable to parse timestamp with expected
     *                        format
     */
    public String normalizeTimestamp(String timestamp) throws ParseException {

        // Setup timestamp format/timezone. Example timestamp: 10/2/04 8:44:11 AM
        DateFormat readDateFormat = new SimpleDateFormat("MM/dd/yy HH:mm:ss a");
        readDateFormat.setTimeZone(TimeZone.getTimeZone("PST"));

        // Parse timestamp assuming pacific timezone
        Date date = new Date();
        date = readDateFormat.parse(timestamp);

        // Setup desired output format/timezone
        DateFormat writeDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        writeDateFormat.setTimeZone(TimeZone.getTimeZone("EST"));

        // Format date
        String normalizedTimestamp = writeDateFormat.format(date);

        return normalizedTimestamp;
    }

    /**
     * Pad the given zip to 5 digits with 0 as the prefix
     * 
     * @param zip - Raw input zip to be normalized
     * @return - Normalized 5 digit zip
     */
    public String normalizeZip(String zip) {

        StringBuilder sb = new StringBuilder();

        // Pad Zip with 0 prefix until length is 5
        while (sb.length() < 5 - zip.length()) {
            sb.append('0');
        }
        sb.append(zip);

        // Return padded Zip as a String
        return sb.toString();
    }

    /**
     * Normalize a given fullName String by making it upper case
     * 
     * @param fullName - Raw input full name
     * @return - Normalized uppercase full name
     */
    public String normalizeFullName(String fullName) {
        // Convert to upper case
        return fullName.toUpperCase(Locale.getDefault());
    }

    /**
     * Normalize duration by calculating total number of seconds
     * 
     * @param duration - Raw input duration of the format HH:MM:SS.MS format (where
     *                 MS is milliseconds)
     * @return - Normalized duration in seconds
     */
    public String normalizeDuration(String duration) throws NumberFormatException {

        // Split out duration time sections, columns are in HH:MM:SS.MS format
        String[] durationParts = duration.split(":");

        // Calculate total seconds (HH*3600 + MM*60 + SS.MM)
        Double durationSec = Double.parseDouble(durationParts[0]) * 3600 + Double.parseDouble(durationParts[1]) * 60
                + Double.parseDouble(durationParts[2]);

        return durationSec.toString();
    }

}