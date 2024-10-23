package cs2110;

import java.util.Set;

public class Operation implements Expression {

    private Operator op;
    private Expression leftOperand;
    private Expression rightOperand;
    int opCount = 1;

    /**
     * Create a node representing the value `value`.
     */
    public Operation(Operator op, Expression leftOperand, Expression rightOperand) {
        this.op = op;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.opCount = opCount;
    }

    /**
     * Assert whether invariants meets it's specification
     * */
    public void assertInv(){
        assert (leftOperand != null);
        assert (rightOperand != null);
        assert (op != null);
    }

    /**
     * Return this node's value.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException{
        assertInv();
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * Return the number of operation required.
     */
    @Override
    public int opCount() {
        opCount += leftOperand.opCount() + rightOperand.opCount();
        assertInv();
        return opCount;
    }

    @Override
    public String postfixString(){
        assertInv();
        String opString = op.symbol();
        String rightOpString = rightOperand.postfixString();
        String leftOpString = rightOperand.postfixString();

        String postFixString = rightOpString + leftOpString + opString;

        assertInv();
        return postFixString;
    }

    @Override
    public String infixString(){
        assertInv();
        String opString = " " + op.symbol() + " ";
        String rightOpString = rightOperand.infixString();
        String leftOpString = leftOperand.infixString();

        String inFixString = "(" + leftOpString +  opString + rightOpString + ")";

        assertInv();
        return inFixString;
    }

    @Override
    public Expression optimize(VarTable vars){
        // TODO
        throw new RuntimeException();
    }

    @Override
    public Set<String> dependencies(){
        // TODO
        throw new RuntimeException();
    }

    @Override
    public boolean equals(Object other) {
        Operation otherOperation = (Operation) other;

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        if (this == other) {
            return true;
        }

        return op.equals(otherOperation.op) &&
                leftOperand.equals(otherOperation.leftOperand) &&
                rightOperand.equals(otherOperation.rightOperand);
    }
}