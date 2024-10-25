package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.Scanner;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CsvEvaluatorTest {

    @Test
    @DisplayName("The column label for column 0 should be the empty string")
    void testColToLetters0() {
        assertEquals("", CsvEvaluator.colToLetters(0));
    }

    @Test
    @DisplayName("Column labels for columns 1-26 should consist of the correct single letter")
    void testColToLetters1() {
        assertEquals("A", CsvEvaluator.colToLetters(1));
        assertEquals("Z", CsvEvaluator.colToLetters(26));
    }

    @Test
    @DisplayName("Column labels for columns 27-704 should consist of the correct two letters")
    void testColToLetters2() {
        assertEquals("AA", CsvEvaluator.colToLetters(27));
        assertEquals("AB", CsvEvaluator.colToLetters(28));
        assertEquals("ZY", CsvEvaluator.colToLetters(701));
        assertEquals("ZZ", CsvEvaluator.colToLetters(702));
    }

    @Test
    @DisplayName("Column labels for columns 703-18278 should consist of the correct three letters")
    void testColToLetters3() {
        assertEquals("AAA", CsvEvaluator.colToLetters(703));
        assertEquals("AAB", CsvEvaluator.colToLetters(704));
        assertEquals("AMJ", CsvEvaluator.colToLetters(1024));
        assertEquals("XFD", CsvEvaluator.colToLetters(16384));
        assertEquals("ZZY", CsvEvaluator.colToLetters(18277));
        assertEquals("ZZZ", CsvEvaluator.colToLetters(18278));
    }



    @Test
    @DisplayName("A spreadsheet containing only constants should not be modified when evaluating " +
            "its formulas")
    void testEvaluateCsvConstant() throws IOException {
        String input = "x,1.5\n";
        String expected = "x,1.5\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("A spreadsheet with a formula referencing a cell on a previous row should " +
            "evaluate correctly.")
    void testEvaluateCsvAboveRef() throws IOException {
        String input = "x,1.5\n" +
                "y,=B1 4 * 1 +\n";
        String expected = "x,1.5\n"
                + "y,7.0\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("A spreadsheet with a formula referencing a previous cell on the same row " +
            "should evaluate correctly.")
    void testEvaluateCsvLeftRef() throws IOException {
        String input = "x,1.5,=B1 4 * 1 +\n";
        String expected = "x,1.5,7.0\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("A spreadsheet with a formula referencing a previous formula should evaluate " +
            "correctly.")
    void testEvaluateCsvFormulaRef() throws IOException {
        String input = "x,1.5\n" +
                "y,=B1 4 * 1 +\n" +
                "z,=B1 B2 *\n";
        String expected = "x,1.5\n"
                + "y,7.0\n"
                + "z,10.5\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        System.out.println(output.toString());
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("A spreadsheet formula referencing a cell that does not contain a number should " +
            "evaluate to #N/A.")
    void testEvaluateCsvNonNumericRef() throws IOException {
        String input = "x,1.5\n" +
                "w,=A1\n";
        String expected = "x,1.5\n"
                + "w,#N/A\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }


    // * Formulas with conditional expressions: correct evaluation
    @Test
    @DisplayName("A spreadsheet formula with conditional expressions should have correct " +
            "evaluation.")
    void testEvaluateCsvWithCon() throws IOException {
        String input = "x,2.5\n" +
                "y,=1 6 8 ?:\n";
        String expected = "x,2.5\n" +
                "y,6.0\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }

    // * Formulas with known function applications: correct evaluation
    @Test
    @DisplayName("A formula with known function application should have correct " +
            "evaluation")
    void testEvaluateCsvKnownFunction() throws IOException {
        // A formula that uses an unsupported function "sqrt"
        String input = "x,3.14159\n" +
                "y,=16 sqrt()\n";
        String expected = "x,3.14159\n" +
                "y,4.0\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }


    // * Formulas with unknown function applications: #N/A
    @Test
    @DisplayName("A formula with an unknown function application should evaluate to #N/A")
    void testEvaluateCsvUnknownFunction() throws IOException {
        // A formula that uses an unsupported function "sqrt"
        String input = "x,6\n" +
                "y,=A1 unknown()\n";
        String expected = "x,6\n" +
                "y,#N/A\n";

        StringBuilder output = new StringBuilder();
        CsvEvaluator.evaluateCsv(CsvEvaluator.SIMPLIFIED_CSV.parse(new StringReader(input)),
                CsvEvaluator.SIMPLIFIED_CSV.print(output));
        assertEquals(expected, output.toString());
    }

    // * Formulas with future cell references (to numbers or other formulas): #N/A

    // * Formulas with out-of-bounds cell references: #N/A

    // * Formulas with variables that do not correspond to a cell coordinate: #N/A
    
    // * Formulas containing an incomplete RPN expression: #N/A


}