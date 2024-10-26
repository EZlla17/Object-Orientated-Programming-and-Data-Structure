package cs2110;

import java.util.HashSet;
import java.util.Set;

/**
 * An expression tree node representing an operation node representing arithmetic operations.
 */
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
     */
    public void assertInv() {
        assert (leftOperand != null);
        assert (rightOperand != null);
        assert (op != null);
    }

    /**
     * Evaluate the operation by calling operate method on left operant and right operant.
     *
     * @param vars variable table for linking variables to constants.
     *             return the evaluation.
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        assertInv();
        return op.operate(leftOperand.eval(vars), rightOperand.eval(vars));
    }

    /**
     * Return the number of operation required.
     */
    @Override
    public int opCount() {
        return leftOperand.opCount() + rightOperand.opCount() + 1;
    }

    /**
     * Generates the postfixString notation of this `Operation`.
     * return a string of the postfix notation where the postfix notation of
     * left operand is followed by the postfix notation of the right operand
     * and end with the operator's symbol.
     */
    @Override
    public String postfixString() {
        assertInv();
        String opString = op.symbol() + " ";
        String rightOpString = rightOperand.postfixString() + " ";
        String leftOpString = leftOperand.postfixString() + " ";

        String postFixString = leftOpString + rightOpString + opString;

        return postFixString.strip();
    }

    /**
     * Generates the infixString notation of this `Operation`.
     * return a string of the postfix notation where the postfix notation of
     * left operand is followed by the operator's symbol and end with the
     * postfix notation of the right operand.
     */
    @Override
    public String infixString() {
        assertInv();
        String opString = " " + op.symbol() + " ";
        String rightOpString = rightOperand.infixString();
        String leftOpString = leftOperand.infixString();

        String inFixString = "(" + leftOpString + opString + rightOpString + ")";

        assertInv();
        return inFixString;
    }

    /**
     * Optimizes the expression by recursively optimizing the left and right operand.
     *
     * @param vars variable table for linking variables to constants.
     *             return the optimized operation.
     */
    @Override
    public Expression optimize(VarTable vars) {
        leftOperand = leftOperand.optimize(vars);
        rightOperand = rightOperand.optimize(vars);
        if (rightOperand instanceof Constant
                && leftOperand instanceof Constant) {
            try {
                return new Constant(eval(vars));
            } catch (UnboundVariableException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    /**
     * Find all variable dependencies from the left operand and right operand.
     *
     * @return a set of these variable dependencies of this `Operation` expression.
     */
    @Override
    public Set<String> dependencies() {
        Set<String> newSet = new HashSet<>();
        newSet.addAll(leftOperand.dependencies());
        newSet.addAll(rightOperand.dependencies());
        return newSet;
    }

    /**
     * Checks whether this `Operation` expression is equal to `other`
     *
     * @param other the object to be compared with this `Operation`
     *              Return true if `other` is an instance of `Operation`,
     *              or has the same operator and operand values as this `Operation`.
     *              Otherwise, return false.
     */
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