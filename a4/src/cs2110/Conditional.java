package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Conditional implements Expression {
    private Expression trueBranch;
    private Expression falseBranch;
    private Expression condition;

    /**
     * Create a node representing the conditional node.
     */
    public Conditional(Expression condition, Expression trueBranch, Expression falseBranch) {
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.condition = condition;
    }

    /**
     * Assert whether invariants meets it's specification
     */
    public void assertInv() {
        assert (trueBranch != null);
        assert (falseBranch != null);
        assert (condition != null);
    }

    /**
     * Return this node's value. If condition is 0.0 return the value of falseBranch.
     * Otherwise, return the value of the trueBranch.
     * @param vars variable table for linking variables to constants
     */
    @Override
    public double eval(VarTable vars) throws UnboundVariableException {
        if (condition.eval(vars) == 0.0) {
            return falseBranch.eval(vars);
        } else {
            return trueBranch.eval(vars);
        }
    }

    /**
     * Return the number of operation required.
     */
    @Override
    public int opCount() {
        int result = 1 + condition.opCount() + Math.max(trueBranch.opCount(), falseBranch.opCount());

        return result;
    }

    /**
     * Generates the postfix notation of this `Conditional` expression.
     * Return a `String` in postfix notation where the condition is followed
     * by the true and false branches,and ending with the `?:` operator.
     */
    @Override
    public String postfixString() {
        String trueString = trueBranch.postfixString() + " ";
        String falseString = falseBranch.postfixString() + " ";
        String conditionString = condition.postfixString() + " ";

        String postFixString = conditionString + trueString + falseString + "?:";

        assertInv();
        return postFixString.strip();
    }

    /**
     * Generates the infix notation of this `Conditional` expression.
     * Return a string in infix notation where the condition is followed by the `?` operator,
     * then the true branch, then the `:` operator, then end with the false branch.
     */
    @Override
    public String infixString() {
        String trueString = trueBranch.infixString();
        String falseString = falseBranch.infixString();
        String conditionString = condition.infixString();

        assertInv();
        return "(" + conditionString + " ? " + trueString + " : " + falseString + ")";
    }


    /**
     * Optimizes this `Conditional` expression by recursively optimizes `trueBranch`, `falseBranch`,
     * and `condition`.
     * @param vars variable table for linking variables to constants
     * return the optimized expression, or this when there's no possible simplification.
     */
    public Expression optimize(VarTable vars) {
        trueBranch = trueBranch.optimize(vars);
        falseBranch = falseBranch.optimize(vars);
        condition = condition.optimize(vars);
        if (condition instanceof Constant) {
            try {
                if (condition.eval(vars) == 0) {
                    return falseBranch;
                } else {
                    return trueBranch;
                }
            } catch (UnboundVariableException e) {
                throw new RuntimeException(e);
            }
        }
        return this;
    }

    /**
     * Find all variable dependencies from the `trueBranch`, `falseBranch`, and `condition` expressions.
     * @return a set of variable dependencies of this `Conditional` expression.
     */
    public Set<String> dependencies() {
        Set<String> newSet = new HashSet<>();
        newSet.addAll(trueBranch.dependencies());
        newSet.addAll(falseBranch.dependencies());
        newSet.addAll(condition.dependencies());
        return newSet;
    }

    /**
     * Check whether this `Conditional` expression is equal to another.
     * Two `Conditional` expressions are equal if they are the same instance,
     * or if their `trueBranch`, `falseBranch`, and `condition` are equal.
     * @param other is the object to be compared with this `Conditional`.
     * return true if `other` is a `Conditional` that is equal to this instance;
     * otherwise, return false.
     */
    @Override
    public boolean equals(Object other) {
        Conditional otherCondition = (Conditional) other;

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        if (this == other) {
            return true;
        }

        return trueBranch.equals(otherCondition.trueBranch) &&
                falseBranch.equals(otherCondition.falseBranch) &&
                condition.equals(otherCondition.condition);
    }
}
