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
    @DisplayName("A new table should have a size of 0 and should not contain anything")
    void testNewTableEmptySizeAndNames() {
        TreeVarTable varTable = new TreeVarTable();
        assertEquals(0, varTable.size(), "New table should have size 0");
        assertTrue(varTable.names().isEmpty(), "New table should have an empty set of names");
    }

    @Test
    @DisplayName("All assigned variables should be in a table's names, including updates")
    void testAllAssignedVariablesInNames() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("a", 1.0);
        varTable.set("b", 2.0);
        varTable.set("c", 3.0);

        Set<String> expectedNames = new HashSet<>();
        expectedNames.add("a");
        expectedNames.add("b");
        expectedNames.add("c");

        assertEquals(expectedNames, varTable.names(), "All assigned variables should appear in names");

        // Update an existing variable and check names again
        varTable.set("b", 2.5);
        assertEquals(expectedNames, varTable.names(), "Updating a variable should not change names");
    }

    @Test
    @DisplayName("Behavior should match MapVarTable for get, set, and unset")
    void testBehaviorMatchesMapVarTable() throws UnboundVariableException {
        TreeVarTable treeTable = new TreeVarTable();
        MapVarTable mapTable = MapVarTable.of("x", 5.0, "y", 10.0);

        // Initial setting and getting
        treeTable.set("x", 5.0);
        treeTable.set("y", 10.0);

        assertEquals(mapTable.get("x"), treeTable.get("x"));
        assertEquals(mapTable.get("y"), treeTable.get("y"));

        // Update values in both tables and check consistency
        treeTable.set("x", 7.0);
        mapTable.set("x", 7.0);
        assertEquals(mapTable.get("x"), treeTable.get("x"));

        // Unset a variable and compare size and names
        treeTable.unset("y");
        mapTable.unset("y");
        assertEquals(mapTable.size(), treeTable.size());
        assertEquals(mapTable.names(), treeTable.names());
    }

    @Test
    @DisplayName("An empty table should return an empty set of names")
    void testEmptyTableNames() {
        TreeVarTable varTable = new TreeVarTable();
        assertTrue(varTable.names().isEmpty(), "Names set should be empty for a new table");
    }

    @Test
    @DisplayName("Unsetting a variable not in the table should not change the size")
    void testUnsetNonExistingVariable() {
        TreeVarTable varTable = new TreeVarTable();
        varTable.set("existing", 1.0);
        int initialSize = varTable.size();

        varTable.unset("nonexistent");
        assertEquals(initialSize, varTable.size(), "Size should not change when unsetting a non-existent variable");
    }

    @Test
    @DisplayName("Setting a new variable should increase the size by 1 and it should be retrievable")
    void testSetNewVariableIncreasesSize() throws UnboundVariableException {
        TreeVarTable varTable = new TreeVarTable();
        int initialSize = varTable.size();

        varTable.set("newVar", 1.0);
        assertEquals(initialSize + 1, varTable.size(), "Size should increase by 1 after setting a new variable");
        assertEquals(1.0, varTable.get("newVar"), "New variable should be retrievable with the correct value");
    }
}