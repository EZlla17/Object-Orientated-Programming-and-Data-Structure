package cs2110;

import java.util.HashSet;
import java.util.Set;

/**
 * A VarTable implemented using a Binary search Tree algorithm. all variables are storied in a tree structure
 */
public class TreeVarTable implements VarTable {
    /**
     * Invariants:
     * 1. Binary Search Order: For each node in the tree:
     *      - All nodes in the `less` subtree have `data` values less than this node’s `data`.
     *      - All nodes in the `more` subtree have `data` values greater than this node’s `data`.
     * 2. Binary Tree Structure: Each node has at most two children (`less` and `more`),
     *    which are either `null` or instances of `TreeVarTable`.
     * 3. Unique Variable Names: Each `name` field is unique within the entire tree.
     * 4. Variable Name Nullity:
     *      - The `name` field can only be `null` for the root node when the tree is empty.
     *      - For all other nodes, `name` is non-null.
     * 5. Leaf Node Condition: A node with `less == null` and `more == null` is considered a leaf node,
     *    having no further descendants.
     * 6. Root Data Validity: If the root node's `name` is non-null, then `data` must contain the value
     *    associated with `name`.
     */

    /**
     *  a String name of this variable
     */
    String name = null;

    /**
     *  the data is stored with this name
     */
    double data;

    /**
     *  a node of a tree that its & its children's (if any) values are all smaller or equal to data of this node
     */
    TreeVarTable less = null;

    /**
     *  a node of a tree that its & its children's (if any) values are all greater to data of this node
     */
    TreeVarTable more = null;

    /**
     * Return the value associated with the variable `name`.  Throws UnboundVariableException if
     * `name` is not associated with a value in this table.
     */
    public double get(String inputName) throws UnboundVariableException {
        assert inputName != null;

        if (this.name == null){
            throw new UnboundVariableException(inputName);
        }
        if (inputName.equals(name)) {
            return data;
        }
        if (more != null) {
            try {
                return more.get(inputName);
            } catch (UnboundVariableException e) {
            }
        }
        if (less != null) {
            return less.get(inputName);
        }
        throw new UnboundVariableException(inputName);
    }

    /**
     * Associate `value` with variable `name` in this table, replacing any previously assigned value.
     */
    public void set(String name, double value) {
        assert name != null;

        // Base case: root node
        if (this.name == null) {
            this.name = name;
            this.data = value;
            return;
        }

        // If updating the root node value
        if (this.name.equals(name)) {
            this.data = value;
            return;
        }

        // Recursive case: check if the value goes in the `less` or `more` subtree
        if (value <= this.data) {
            // If `less` node is null, initialize it and set value there
            if (this.less == null) {
                this.less = new TreeVarTable();
            }
            this.less.set(name, value);
        } else {
            // If `more` node is null, initialize it and set value there
            if (this.more == null) {
                this.more = new TreeVarTable();
            }
            this.more.set(name, value);
        }
    }

    /**
     * Remove any value associated with variable `name` in this table.
     */
    public void unset(String name) {
        assert name != null;

        // Unsetting the only node in the tree
        if (name.equals(this.name) && less == null && more == null) {
            this.name = null;
            this.data = 0;
            return;
        }

        // Unsetting a leaf node
        if (less != null && less.name.equals(name)) {
            if (less.leafCheck()) {
                less = null;
                return;
            }
        } else if (more != null && more.name.equals(name)) {
            if (more.leafCheck()) {
                more = null;
                return;
            }
        }

        // Unsetting a node with both children (merge `less` and `more` subtrees)
        if (name.equals(this.name) && less != null && more != null) {
            TreeVarTable mergedSubtree = merge(less, more);
            this.name = mergedSubtree.name;
            this.data = mergedSubtree.data;
            this.less = mergedSubtree.less;
            this.more = mergedSubtree.more;
            return;
        }

        // Unsetting a node with only one child
        if (name.equals(this.name)) {
            if (less != null) {
                this.name = less.name;
                this.data = less.data;
                this.more = less.more;
                this.less = less.less;
            } else if (more != null) {
                this.name = more.name;
                this.data = more.data;
                this.less = more.less;
                this.more = more.more;
            }
            return;
        }

        // Recursively search for the variable to unset in left or right subtree
        if (less != null) {
            less.unset(name);
        }
        if (more != null) {
            more.unset(name);
        }
    }

    /**
     * Merge two children's nodes into one node, only apply to a node with both more and less node.
     * Requires both left or right node is not null.
     * Return a new parent node, which is the original input 'left' node; the 'right' node is
     * merged to the 'more' node in the input 'left' node.
     * */
    public TreeVarTable merge(TreeVarTable left,TreeVarTable right){
        assert left != null;
        assert right != null;

        if (left.more == null){
            left.more = right;
            return left;
        }

        TreeVarTable temp = left.more;
        left.more = merge(temp, right);
        return left;
    }

    /**
     * Return whether if this node is a leaf node, meaning that no less or more node
     */
    private boolean leafCheck(){
        if (less == null && more == null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Return whether variable `name` is currently associated with a value in this tree.
     */
    public boolean contains(String name){
        assert name != null;

        if (this.name != null && this.name.equals(name)) {
            return true;
        }
        if (less == null && more == null) {
            return false;
        }
        return (less != null && less.contains(name)) || (more != null && more.contains(name));
    }

    /**
     * Return the number of variables associated with values in this tree.
     */
    public int size(){
        int count = (name != null) ? 1 : 0;

        if (less != null) {
            count += less.size();
        }
        if (more != null) {
            count += more.size();
        }
        return count;
    }

    /**
     * Return the names of all variables associated with a value in this tree.
     */
    public Set<String> names(){
        Set<String> result = new HashSet<>();

        if (less == null && more == null && name == null){
            return result;
        }
        if (less == null && more == null && name != null){
            result.add(name);
            return result;
        }
        else{
            Set<String> resultRecursive = new HashSet<>();
            resultRecursive.add(name);
            if (less != null){
                resultRecursive.addAll(less.names());
            }
            if (more != null){
                resultRecursive.addAll(more.names());
            }
            return resultRecursive;
        }
    }

    /**
     * Create a new empty TreeVarTable.
     */
    public static TreeVarTable empty() {
        return new TreeVarTable();
    }

    /**
     * Create a TreeVarTable associating `value` with variable `name`.
     */
    public static TreeVarTable of(String name, double value) {
        assert name != null;

        TreeVarTable ans = new TreeVarTable();
        ans.set(name, value);
        return ans;
    }

    /**
     * Create a TreeVarTable associating `value1` with variable `name1` and `value2` with `name2`.
     */
    public static TreeVarTable of(String name1, double value1, String name2, double value2) {
        assert name1 != null;
        assert name2 != null;

        TreeVarTable ans = new TreeVarTable();
        ans.set(name1, value1);
        ans.set(name2, value2);
        return ans;
    }
}
