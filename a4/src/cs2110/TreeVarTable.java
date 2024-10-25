package cs2110;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class TreeVarTable implements VarTable {


    String name = null;
    double data;
    TreeVarTable less = null;
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

        //Unsetting the only node in the tree
        if (name.equals(this.name) && less == null && more == null) {
            this.name = null;
            this.data = 0;
            return;
        }

        //Unsetting a leaf node
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

        //Unsetting a node with both children (merge `less` and `more` subtrees)
        if (name.equals(this.name) && less != null && more != null) {
            TreeVarTable mergedSubtree = merge(less, more);
            this.name = mergedSubtree.name;
            this.data = mergedSubtree.data;
            this.less = mergedSubtree.less;
            this.more = mergedSubtree.more;
            return;
        }

        // Case 4: Unsetting a node with only one child
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

        //Recursively search for the variable to unset in left or right subtree
        if (less != null) {
            less.unset(name);
        }
        if (more != null) {
            more.unset(name);
        }
    }


    /**
     * Merge two children's nodes into one node, only apply to a node with both more and less node
     */
    public TreeVarTable merge(TreeVarTable left,TreeVarTable right){
        if (left.more == null){
            left.more = right;
            return left;
        }
        TreeVarTable temp = left.more;
        left.more = merge(temp, right);
        return left;
    }

    /**
     * Check whether if this node is a leaf
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
     * Return whether variable `name` is currently associated with a value in this table.
     */
    public boolean contains(String name){
        if (this.name != null && this.name.equals(name)) {
            return true;
        }
        if (less == null && more == null) {
            return false;
        }
        return (less != null && less.contains(name)) || (more != null && more.contains(name));
    }

    /**
     * Return the number of variables associated with values in this table.
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
     * Return the names of all variables associated with a value in this table.
     */
    public Set<String> names(){
        Set<String> result = new HashSet<>();

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
}
