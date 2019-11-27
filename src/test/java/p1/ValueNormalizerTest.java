package p1;

import java.text.ParseException;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class ValueNormalizerTest {
    private ValueNormalizer valueNormalizer;

    @Before
    public void before() {
        // Init valueNormalizer used in all tests
        valueNormalizer = new ValueNormalizer();
    }

    /**
     * Test normalizing a basic, well-formatted timestamp
     */
    @Test
    public void testNormalizeTimestamp() {

        try {
            String normalizedValue = valueNormalizer.normalizeTimestamp("10/2/04 8:44:11 AM");
            assertTrue(normalizedValue.equals("2004-10-02T10:44:11.000-05:00"));
        } catch (ParseException ex) {
            // No exception should be raised from valid date
            assertTrue(false);
        }
    }

    /**
     * Test normalizing an invalid timestamp throws expected exception (no AM/PM)
     */
    @Test
    public void testNormalizeBadTimestamp() {

        try {
            String normalizedValue = valueNormalizer.normalizeTimestamp("10/2/04 8:44:11");

            // Should not make it here, exception should have been raised
            assertTrue(false);

        } catch (ParseException ex) {
            // Expect to get a ParseException with detail about the exception
            assertTrue(ex.getMessage().equals("Unparseable date: \"10/2/04 8:44:11\""));
        }
    }

    /**
     * Test padding a short zip to 5 chars
     */
    @Test
    public void testZipLengthPadding() {

        String normalizedValue = valueNormalizer.normalizeZip("1");
        assertTrue(normalizedValue.equals("00001"));
    }

    /**
     * Test padding a zip that is already 5 chars long
     */
    @Test
    public void testFullZipLengthPadding() {

        String normalizedValue = valueNormalizer.normalizeZip("12345");
        assertTrue(normalizedValue.equals("12345"));
    }

    /**
     * Test padding an empty Zip
     */
    @Test
    public void testEmptyZipLengthPadding() {

        String normalizedValue = valueNormalizer.normalizeZip("");
        assertTrue(normalizedValue.equals("00000"));
    }

    /**
     * Test full name is normalized to upper case
     */
    @Test
    public void testUpperCaseFullName() {

        String normalizedValue = valueNormalizer.normalizeFullName("fullName");
        assertTrue(normalizedValue.equals("FULLNAME"));
    }

    /**
     * Test normalizing an empty full name
     */
    @Test
    public void testEmptyFullName() {

        String normalizedValue = valueNormalizer.normalizeFullName("");
        assertTrue(normalizedValue.equals(""));
    }

    /**
     * Test normalizing a full name with special characters
     */
    @Test
    public void testSpecialCharFullName() {

        String normalizedValue = valueNormalizer.normalizeFullName("Réÿs🏴mé 🍎😍n");
        assertTrue(normalizedValue.equals("RÉŸS🏴MÉ 🍎😍N"));
    }

    /**
     * Test normalizing a full name with forigen characters
     */
    @Test
    public void testForigenFullName() {

        String normalizedValue = valueNormalizer.normalizeFullName("株式会社スタジオジブリ");
        assertTrue(normalizedValue.equals("株式会社スタジオジブリ"));
    }

    /**
     * Test normalizing a well-formatted duration
     */
    @Test
    public void testNormalizeDuration() {

        try {
            String normalizedValue = valueNormalizer.normalizeDuration("01:25:36.159");
            assertTrue(normalizedValue.equals("5136.159"));
        } catch (NumberFormatException ex) {
            assertTrue(false);
        }
    }

    /**
     * Test normalizing a duration with letters improperly present throws expected
     * exception
     */
    @Test
    public void testNormalizeBadDuration() {

        try {
            String normalizedValue = valueNormalizer.normalizeDuration("0a:25:36.159");
            assertTrue(false);
        } catch (NumberFormatException ex) {
            assertTrue(ex.getMessage().equals("For input string: \"0a\""));
        }
    }

    /**
     * Test normalizeValue calles different normalization functions depending on
     * column name
     */
    @Test
    public void testNormalizeValue() {

        try {
            String normalizedTimestamp = valueNormalizer.normalizeValue("Timestamp", "10/2/04 8:44:11 AM");
            assertTrue(normalizedTimestamp.equals("2004-10-02T10:44:11.000-05:00"));

            String normalizedZip = valueNormalizer.normalizeValue("ZIP", "1");
            assertTrue(normalizedZip.equals("00001"));

            String normalizedFullName = valueNormalizer.normalizeValue("FullName", "testName");
            assertTrue(normalizedFullName.equals("TESTNAME"));

            String normalizedFooDuration = valueNormalizer.normalizeValue("FooDuration", "01:25:36.159");
            assertTrue(normalizedFooDuration.equals("5136.159"));

            String normalizedBarDuration = valueNormalizer.normalizeValue("BarDuration", "01:25:37.132");
            assertTrue(normalizedBarDuration.equals("5137.132"));

            String normalizedOtherField = valueNormalizer.normalizeValue("OtherField", "testName");
            assertTrue(normalizedOtherField.equals("testName"));
        } catch (Exception ex) {
            // No exception should be raised
            assertTrue(false);
        }
    }
}
