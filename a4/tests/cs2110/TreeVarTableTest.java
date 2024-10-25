package cs2110;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.HashSet;

class TreeVarTableTest {

    @Test
    @DisplayName("Setting and retrieving a variable in TreeVarTable")
    void testSetAndGet() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("s", 2.0);
        System.out.println(varTable.names());
        assertEquals(2.0, varTable.get("s"));
    }

    @Test
    @DisplayName("Retrieving a non-existing variable should throw UnboundVariableException")
    void testGetUnboundWithouValue() {
        TreeVarTable varTable = new TreeVarTable();
        assertThrows(UnboundVariableException.class, () -> varTable.get("y"));
    }

    @Test
    @DisplayName("Retrieving a non-existing variable should throw UnboundVariableException")
    void testGetUnboundWithValue() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        assertThrows(UnboundVariableException.class, () -> varTable.get("y"));
    }

    @Test
    @DisplayName("Updating an existing variable's value in TreeVarTable")
    void testSetUpdate() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        System.out.println(varTable.names());
        varTable.set("x", 3.5);
        System.out.println(varTable.names());
        assertEquals(3.5, varTable.get("x"));
    }

    @Test
    @DisplayName("Unset a variable that does not exist")
    void testUnsetUnexist() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("y", 2.0);
        varTable.unset("x");
        assertThrows(UnboundVariableException.class, () -> varTable.get("x"));
    }

    @Test
    @DisplayName("Unset a variable that does exist")
    void testUnsetExist() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        varTable.unset("x");
        System.out.println(varTable.names());
        System.out.println(varTable.size());
        assertTrue(0 == varTable.size());
    }

    @Test
    @DisplayName("Unset a non-leaf node with both children and check if it merges correctly")
    void testUnsetWithMerging() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 5.0);
        varTable.set("a", 3.0);
        varTable.set("z", 8.0);
        varTable.unset("x");

        assertTrue(varTable.contains("a"));
        assertTrue(varTable.contains("z"));
    }

    @Test
    @DisplayName("Check contains for existing and non-existing variables")
    void testContains() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        varTable.set("y", 3.0);
        assertTrue(varTable.contains("x"));
        assertTrue(varTable.contains("y"));
        assertFalse(varTable.contains("z"));
    }

    @Test
    @DisplayName("Check size after adding and removing variables")
    void testSize() {
        TreeVarTable varTable = new TreeVarTable();
        assertEquals(0, varTable.size());

        varTable.set("x", 2.0);
        assertEquals(1, varTable.size());

        varTable.set("y", 3.0);
        varTable.set("z", 5.0);
        assertEquals(3, varTable.size());

        varTable.unset("y");
        assertEquals(2, varTable.size());
    }

    @Test
    @DisplayName("Check size after adding and removing variables")
    void testSizeParent() {
        TreeVarTable varTable = new TreeVarTable();
        assertEquals(0, varTable.size());

        varTable.set("x", 2.0);
        assertEquals(1, varTable.size());

        varTable.set("y", 3.0);
        varTable.set("z", 5.0);
        assertEquals(3, varTable.size());

        varTable.unset("x");
        System.out.println(varTable.names());
        assertEquals(2, varTable.size());
    }

    @Test
    @DisplayName("Verify that names method returns correct set of variable names")
    void testNameOne() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);

        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("x");

        System.out.println(expectedNames);
        System.out.println(varTable.names());
        assertEquals(expectedNames, varTable.names());
    }

    @Test
    @DisplayName("Verify that names method returns correct set of variable names")
    void testNamesTwo() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        varTable.set("y", 3.0);

        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("x");
        expectedNames.add("y");

        System.out.println(expectedNames);
        System.out.println(varTable.names());
        assertEquals(expectedNames, varTable.names());
    }

    @Test
    @DisplayName("Verify that names method returns correct set of variable names")
    void testNames() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("x", 2.0);
        varTable.set("y", 3.0);
        varTable.set("z", 5.0);

        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("x");
        expectedNames.add("y");
        expectedNames.add("z");

        System.out.println(expectedNames);
        System.out.println(varTable.names());
        assertEquals(expectedNames, varTable.names());
    }

    @Test
    @DisplayName("Check merging function works with left and right subtrees")
    void testMergeFunctionality() {
        TreeVarTable left = new TreeVarTable();
        left.set("a", 1.0);
        left.set("b", 2.0);

        TreeVarTable right = new TreeVarTable();
        right.set("c", 3.0);
        right.set("d", 4.0);

        TreeVarTable merged = left.merge(left, right);
        assertTrue(merged.contains("a"));
        assertTrue(merged.contains("b"));
        assertTrue(merged.contains("c"));
        assertTrue(merged.contains("d"));
    }

    @Test
    @DisplayName("Set method: Adding new variables to TreeVarTable")
    void testSetNewVariables() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();

        // Set root variable
        varTable.set("root", 10.0);
        assertEquals(10.0, varTable.get("root"));

        // Add variable less than root (should go to the 'less' subtree)
        varTable.set("left", 5.0);
        assertEquals(5.0, varTable.get("left"));
        assertTrue(varTable.contains("left"));

        // Add variable greater than root (should go to the 'more' subtree)
        varTable.set("right", 15.0);
        assertEquals(15.0, varTable.get("right"));
        assertTrue(varTable.contains("right"));
    }

    @Test
    @DisplayName("Set method: Updating an existing variable's value")
    void testSetUpdateExistingValue() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();

        // Set variable and then update its value
        varTable.set("x", 5.0);
        assertEquals(5.0, varTable.get("x"));

        varTable.set("x", 7.5); // Update value
        assertEquals(7.5, varTable.get("x"));
    }

    @Test
    @DisplayName("Set method: Handling deeper tree structure")
    void testSetNestedVariables() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();

        // Creating a nested structure
        varTable.set("root", 10.0);
        varTable.set("left", 5.0);
        varTable.set("right", 15.0);
        varTable.set("leftLeft", 2.0);
        varTable.set("leftRight", 7.0);
        varTable.set("rightLeft", 12.0);
        varTable.set("rightRight", 17.0);

        // Verify that all values were set correctly in a nested structure
        assertEquals(10.0, varTable.get("root"));
        assertEquals(5.0, varTable.get("left"));
        assertEquals(15.0, varTable.get("right"));
        assertEquals(2.0, varTable.get("leftLeft"));
        assertEquals(7.0, varTable.get("leftRight"));
        assertEquals(12.0, varTable.get("rightLeft"));
        assertEquals(17.0, varTable.get("rightRight"));
    }

    @Test
    @DisplayName("Set method: Handle adding to empty node (null root scenario)")
    void testSetEmptyNode() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();

        // Adding to an empty node
        varTable.set("first", 3.0);

        // The root node should now hold the first set value
        assertEquals(3.0, varTable.get("first"));
        assertTrue(varTable.contains("first"));
    }

    @Test
    @DisplayName("Set method: Add values and verify size updates correctly")
    void testSetAndSize() {
        TreeVarTable varTable = new TreeVarTable();

        // Initially empty
        assertEquals(0, varTable.size());

        // Add values and verify size increments
        varTable.set("a", 1.0);
        assertEquals(1, varTable.size());

        varTable.set("b", 2.0);
        assertEquals(2, varTable.size());

        varTable.set("c", 3.0);
        assertEquals(3, varTable.size());

        // Update an existing variable should not increase size
        varTable.set("a", 1.5);
        assertEquals(3, varTable.size());
    }

    @Test
    @DisplayName("Operations on an initially empty TreeVarTable")
    void testDeeplyNestedTreeOperations() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();

        // Check behavior on an empty table
        assertEquals(0, varTable.size());
        assertFalse(varTable.contains("nonExistent"));
        assertThrows(UnboundVariableException.class, () -> varTable.get("nonExistent"));
        assertTrue(varTable.size() == 0);

        // Create a deeply nested tree structure after verifying initial empty state
        varTable.set("root", 10.0);
        varTable.set("a", 5.0);
        varTable.set("b", 15.0);
        varTable.set("aa", 2.5);
        varTable.set("ab", 7.5);
        varTable.set("ba", 12.5);
        varTable.set("bb", 17.5);
        varTable.set("aaa", 1.25);

        // Test get for various levels of depth
        assertEquals(10.0, varTable.get("root"));
        assertEquals(5.0, varTable.get("a"));
        assertEquals(15.0, varTable.get("b"));
        assertEquals(2.5, varTable.get("aa"));
        assertEquals(7.5, varTable.get("ab"));
        assertEquals(12.5, varTable.get("ba"));
        assertEquals(17.5, varTable.get("bb"));
        assertEquals(1.25, varTable.get("aaa"));

        // Test contains for existing and non-existing variables
        assertTrue(varTable.contains("aa"));
        assertTrue(varTable.contains("aaa"));
        assertFalse(varTable.contains("nonExistent"));

        // Test size after multiple insertions
        assertEquals(8, varTable.size());

        // Test names method
        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("root");
        expectedNames.add("a");
        expectedNames.add("b");
        expectedNames.add("aa");
        expectedNames.add("ab");
        expectedNames.add("ba");
        expectedNames.add("bb");
        expectedNames.add("aaa");
        assertEquals(expectedNames, varTable.names());

        // Test unset on a leaf and check its removal
        varTable.unset("aaa");
        varTable.unset("aa");
        assertFalse(varTable.contains("aaa"));
        assertFalse(varTable.contains("aa"));
        assertEquals(6, varTable.size());

        // Test unset on a non-leaf node and verify structure after merging
        varTable.unset("a");
        assertFalse(varTable.contains("a"));
        assertFalse(varTable.contains("aa"));
        assertTrue(varTable.contains("ab"));
    }
}
