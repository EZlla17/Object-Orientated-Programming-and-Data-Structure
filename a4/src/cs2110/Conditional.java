package cs2110;

import java.util.HashSet;
import java.util.Set;

public class Conditional implements Expression{
    private Expression trueBranch;
    private Expression falseBranch;
    private Expression condition;
    //TODO 我这儿不是很清楚。document上写的是The number of “operations” represented by a Conditional tree should be
    // counted in the worst case; it should include the cost of evaluating the condition（这儿的意思是condition cost是1吗）,
    // plus the cost of the more expensive branch, plus 1 for the action of selecting between them.
    private int opCount = 0;
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
            opCount += falseBranch.opCount();
            return falseBranch.eval(vars);
        } else {
            opCount += trueBranch.opCount();
            return trueBranch.eval(vars);
        }
    }
    /**
     * Return the number of operation required.
     */
    @Override
    public int opCount(){
        //TODO 这个不确定，需要跟rose对一下。
        int conditionCount = condition.opCount();

        int branchCount = (trueBranch.opCount() >= falseBranch.opCount())? trueBranch.opCount(): falseBranch.opCount();

        return 1 + conditionCount + branchCount;
    }

    @Override
    public String postfixString(){
        String trueString = trueBranch.postfixString();
        String falseString = falseBranch.postfixString();
        String conditionString = condition.postfixString() + " ?:";

        String postFixString = conditionString + trueString + falseString;

        return postFixString;
    }

    @Override
    public String infixString(){
        String trueString = trueBranch.infixString();
        String falseString = falseBranch.infixString();
        String conditionString = condition.infixString();

        return "(" + conditionString + " ? " + trueString + " : " + falseString + ")";
    }

    public Expression optimize(VarTable vars){
        // TODO
        throw new RuntimeException();
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
