package cs2110;

import java.util.Set;

public class Variable implements Expression{
    /**
     * The variable of this expression. Should not be empty or null.
     */
    final String name;

    /**
     * Create a node representing the variable.
     */
    public Variable(String name) {
        this.name = name;
    }

    /**
     * Assert the object's invariant that the variable `name` should not be empty or null.
     */
    private void assertInv() {
        assert !name.isEmpty();
        assert name != null;
    }

    /**
     * Return the value of the Variable if the Variable is in vars.
     * If the Variable is not in vars, throw UnboundVariableException.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if (vars.contains(name)) {
            return vars.get(name);
        }
        throw new UnboundVariableException(name);
    }

    /**
     * No operations needed for evaluate the Variable's value.
     */
    @Override
    public int opCount() {
        return 0;
    }

    /**
     * Return the String for the variable name.
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Return the name representation of this node's variable
     */
    @Override
    public String postfixString() {
        return name;
    }

    /**
     * Return whether `other` is equal to this.
     * Return true if `other` has the same name with this.
     * Return false if `other` does not have the same name with this
     */
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != this.getClass()) {
            return false;
        }
        Variable otherVariable = (Variable) other;
        return name.equals(otherVariable.name);
    }


    @Override
    public Expression optimize(VarTable vars) {
        // TODO
        throw new RuntimeException();
    }


    @Override
    public Set<String> dependencies() {
        // TODO
        throw new RuntimeException();
    }
}
