package cs2110;

import java.util.Set;

public class Variable implements Expression{
    /**
     * The client accessible variable to be a number. input should not
     * be null.
     */
    private String name;


    /**
     * Number of operations requires to evaluate this value. opCount must
     * greater or equals to 0
     */
    private int opCount = 0;


    /**
     * Create a node representing the variable 'variable'.
     */
    public Variable(String name) {
        this.name = name;
        this.opCount = opCount;
    }

    private void assertInv() {
        assert opCount >= 0;
        assert name != null;
    }

    /**
     * Return this node's value.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        assertInv();
        return vars.get(name);
    }

    /**
     * Return the number of operations on this variable
     */
    @Override
    public int opCount() {
        assertInv();
        return opCount;
    }

    /**
     * Return the String for the variable name
     */
    @Override
    public String infixString() {
        return name;
    }

    /**
     * Return the String for the variable name
     */
    @Override
    public String postfixString() {
        return name;
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

    @Override
    public boolean equals(Object other) {
        Variable otherVariable = (Variable) other;

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        if (this == other) {
            return true;
        }

        return (name.equals(otherVariable.name));
    }
}
