package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Operation implements Expression {

    /**
     * The operator.
     */
    private Operator op;

    /**
     * The left operand of the operator.
     */
    private Expression leftOperand;

    /**
     * The right operand of the operator.
     */
    private Expression rightOperand;

    //__Resolve__
    private int opCount = 0;

    /**
     * Create an operation with the left operand, the right operand and the operator.
     */
    public Operation(Operator op, Expression leftOperand, Expression rightOperand) {
        this.op = op;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
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
     * Evaluate the operation by calling operate method on left operant and right operant.
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
        opCount += leftOperand.opCount() + rightOperand.opCount() + 1;
        assertInv();
        return opCount;
    }

    @Override
    public String postfixString(){
        assertInv();
        //TODO：这儿的operator不知道要不要加spaces,Helen好像是加了。__Resolve__
        String opString = op.symbol() + " ";
        String rightOpString = rightOperand.postfixString() + " ";
        String leftOpString = leftOperand.postfixString() + " ";

        String postFixString = leftOpString + rightOpString + opString;

        return postFixString.strip();
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
        Set<String> newSet = new HashSet<>();
        newSet.addAll(leftOperand.dependencies());
        newSet.addAll(rightOperand.dependencies());
        return newSet;
    }

    @Override
    public boolean equals(Object other) {

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        if (this == other) {
            return true;
        }

        Operation otherOperation = (Operation) other;

        return op.equals(otherOperation.op) &&
                leftOperand.equals(otherOperation.leftOperand) &&
                rightOperand.equals(otherOperation.rightOperand);
    }
}