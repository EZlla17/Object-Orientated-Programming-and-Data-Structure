package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Conditional implements Expression{
    private Expression trueBranch;
    private Expression falseBranch;
    private Expression condition;

    /**
     * Create a node representing the value `value`.
     */
    public Conditional(Expression condition, Expression trueBranch, Expression falseBranch) {
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        this.condition = condition;
    }

    /**
     * Assert whether invariants meets it's specification
     * */
    public void assertInv(){
        assert (trueBranch != null);
        assert (falseBranch != null);
        assert (condition != null);
    }

    /**
     * Return this node's value.
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
    public int opCount(){
        //TODO 这个不确定，需要跟rose对一下。__Resolve__
        int opCount = 1 + condition.opCount() + Math.max(trueBranch.opCount(), falseBranch.opCount());

        assertInv();
        return opCount;
    }

    @Override
    public String postfixString(){
        String trueString = trueBranch.postfixString() + " ";
        String falseString = falseBranch.postfixString() + " ";
        String conditionString = condition.postfixString() + " ";

        String postFixString = conditionString + trueString + falseString + "?:";

        assertInv();
        return postFixString.strip();
    }

    @Override
    public String infixString(){
        String trueString = trueBranch.infixString();
        String falseString = falseBranch.infixString();
        String conditionString = condition.infixString();

        assertInv();
        return "(" + conditionString + " ? " + trueString + " : " + falseString + ")";
    }

    public Expression optimize(VarTable vars){
        trueBranch = trueBranch.optimize(vars);
        falseBranch = falseBranch.optimize(vars);
        condition = condition.optimize(vars);
        if (condition instanceof Constant){
            try{
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

    public Set<String> dependencies(){
        Set<String> newSet = new HashSet<>();
        newSet.addAll(trueBranch.dependencies());
        newSet.addAll(falseBranch.dependencies());
        newSet.addAll(condition.dependencies());
        return newSet;
    }

    @Override
    public boolean equals(Object other){
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
