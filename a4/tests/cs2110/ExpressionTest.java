package cs2110;

import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantExpressionTest {

    @Test
    @DisplayName("A Constant node should evaluate to its value (regardless of var table)")
    void testEval() throws UnboundVariableException {
        Expression expr = new Constant(1.5);
        assertEquals(1.5, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("A Constant node should report that 0 operations are required to evaluate it")
    void testOpCount() {
        Expression expr = new Constant(1.5);
        assertEquals(0, expr.opCount());
    }

    @Test
    @DisplayName("A Constant node should produce an infix representation with just its value (as " +
            "formatted by String.valueOf(double))")
    void testInfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.infixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.infixString());
    }

    @Test
    @DisplayName("A Constant node should produce an postfix representation with just its value " +
            "(as formatted by String.valueOf(double))")
    void testPostfix() {
        Expression expr = new Constant(1.5);
        assertEquals("1.5", expr.postfixString());

        expr = new Constant(Math.PI);
        assertEquals("3.141592653589793", expr.postfixString());
    }


    @Test
    @DisplayName("A Constant node should equal itself")
    void testEqualsSelf() {
        Expression expr = new Constant(1.5);
        // Normally `assertEquals()` is preferred, but since we are specifically testing the
        // `equals()` method, we use the more awkward `assertTrue()` to make that call explicit.
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Constant node should equal another Constant node with the same value")
    void testEqualsTrue() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(1.5);
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Constant node should not equal another Constant node with a different value")
    void testEqualsFalse() {
        Expression expr1 = new Constant(1.5);
        Expression expr2 = new Constant(2.0);
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Constant node does not depend on any variables")
    void testDependencies() {
        Expression expr = new Constant(1.5);
        Set<String> deps = expr.dependencies();
        assertTrue(deps.isEmpty());
    }


    @Test
    @DisplayName("A Constant node should optimize to itself (regardless of var table)")
    void testOptimize() {
        Expression expr = new Constant(1.5);
        Expression opt = expr.optimize(MapVarTable.empty());
        assertEquals(expr, opt);
    }
}

class VariableExpressionTest {

    @Test
    @DisplayName("A Variable node should evaluate to its variable's value when that variable is " +
            "in the var map")
    void testEvalBound() throws UnboundVariableException {
        MapVarTable varTable = MapVarTable.of("x", 2.0);
        Expression expr = new Variable("x");
        assertEquals(2.0, expr.eval(varTable));
    }

    @Test
    @DisplayName("A Variable node should throw an UnboundVariableException when evaluated if its " +
            "variable is not in the var map")
    void testEvalUnbound() {
        // TODO: Uncomment these lines when you have read about testing exceptions in the handout.
        // They assume that your `Variable` constructor takes its name as an argument.
        Expression expr = new Variable("x");
        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("A Variable node should report that 0 operations are required to evaluate it")
    void testOpCount() {
        Expression expr = new Variable("x");
        assertTrue(expr.opCount() == 0);
    }


    @Test
    @DisplayName("A Variable node should produce an infix representation with just its name")
    void testInfix() {
        Expression expr = new Variable("x");
        assertTrue(expr.infixString().equals("x"));

        Expression expr2 = new Variable("var");
        assertTrue(expr2.infixString().equals("var"));

        Expression expr3 = new Variable("1");
        assertTrue(expr3.infixString().equals("1"));
    }

    @Test
    @DisplayName("A Variable node should produce an postfix representation with just its name")
    void testPostfix() {
        Expression expr = new Variable("x");
        assertTrue(expr.postfixString().equals("x"));

        Expression expr2 = new Variable("var");
        assertTrue(expr2.postfixString().equals("var"));

        Expression expr3 = new Variable("1");
        assertTrue(expr3.postfixString().equals("1"));
    }


    @Test
    @DisplayName("A Variable node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Variable node should equal another Variable node with the same name")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        // Force construction of new String objects to detect inadvertent use of `==`
        Expression expr1 = new Variable(new String("x"));
        Expression expr2 = new Variable(new String("x"));
        assertTrue(expr1.equals(expr2));
        assertFalse(expr1 == expr2);
    }

    @Test
    @DisplayName("A Variable node should not equal another Variable node with a different name")
    void testEqualsFalse() {
        Expression expr1 = new Variable(new String("x"));
        Expression expr2 = new Variable(new String("y"));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Variable node only depends on its name")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertEquals(1, deps.size());
    }


    @Test
    @DisplayName("A Variable node should optimize to a Constant if its variable is in the var map")
    void testOptimizeBound() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("x", 1.5));
        assertEquals(new Constant(1.5), opt);
    }

    @Test
    @DisplayName("A Variable node should optimize to itself if its variable is not in the var map")
    void testOptimizeUnbound() {
        Expression expr = new Variable("x");
        Expression opt = expr.optimize(MapVarTable.of("y", 1.5));
        assertEquals(expr, opt);
    }
}

class OperationExpressionTest {

    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should evaluate to their " +
            "sum")
    void testEvalAdd() throws UnboundVariableException {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Constant(2));
        assertEquals(3.5, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should evaluate " +
            "to its operands' sum when the variable is in the var map")
    void testEvalAddBound() throws UnboundVariableException {
        MapVarTable varTable = MapVarTable.of("x", 2.0);
        Expression var = new Variable("x");
        Expression expr = new Operation(Operator.ADD ,var,new Constant(3.5));
        assertEquals(5.5, expr.eval(varTable));
    }

    @Test
    @DisplayName("An Operation node for DIFFERENT OPERATION with two Constant operands " +
            "should evaluate to their difference")
    void testEvalAll() throws UnboundVariableException {
        Expression expr = new Operation(Operator.SUBTRACT, new Constant(5.0), new Constant(3.0));
        assertEquals(2.0, expr.eval(MapVarTable.empty()));

        Expression expr2 = new Operation(Operator.DIVIDE, new Constant(10.0), new Constant(2.0));
        assertEquals(5.0, expr2.eval(MapVarTable.empty()));

        Expression expr3 = new Operation(Operator.POW, new Constant(2.0), new Constant(3.0));
        assertEquals(8.0, expr3.eval(MapVarTable.empty()));

        Expression expr4 = new Operation(Operator.POW, new Constant(2.0), new Constant(3.0));
        assertEquals(8.0, expr4.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Operation node with multiple levels of nesting should evaluate to the correct result")
    void testEvalComplexNesting() throws UnboundVariableException {
        // Expression: (2 + (3 * (4 - 1))) / 2
        Expression expr = new Operation(Operator.DIVIDE,
                new Operation(Operator.ADD,
                        new Constant(2.0),
                        new Operation(Operator.MULTIPLY,
                                new Constant(3.0),
                                new Operation(Operator.SUBTRACT, new Constant(4.0), new Constant(1.0)))),
                new Constant(2.0));

        assertEquals(5.5, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("An Operation node for ADD with a Variable for an operand should throw an " +
            "UnboundVariableException when evaluated if the variable is not in the var map")
    void testEvalAddUnbound() {
        Expression var = new Variable("x");
        Expression expr = new Operation(Operator.ADD, var, new Constant(3.5));

        assertThrows(UnboundVariableException.class, () -> expr.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Operation node with leaf operands should report that 1 operation is " +
            "required to evaluate it")
    void testOpCountLeaves() {
        MapVarTable varTable = MapVarTable.of("x", 2.0);
        Expression var = new Variable("x");
        Expression expr = new Operation(Operator.ADD ,var,new Constant(3.5));
        assertEquals(expr.opCount(), 1 );
    }


    @Test
    @DisplayName("An Operation node with an Operation for either or both operands should report " +
            "the correct number of operations to evaluate it")
    void testOpCountRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals(2, expr.opCount());

        expr = new Operation(Operator.SUBTRACT,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals(3, expr.opCount());
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce an infix representation " +
            "consisting of its first operand, its operator symbol surrounded by spaces, and " +
            "its second operand, all enclosed in parentheses")
    void testInfixLeaves() {
        MapVarTable varTable = MapVarTable.of("x", 2.0);
        Expression var = new Variable("x");
        Expression expr = new Operation(Operator.ADD ,var,new Constant(3.5));
        assertEquals("(x + 3.5)", expr.infixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected infix representation with parentheses around each operation")
    void testInfixRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("((1.5 * x) + 2.0)", expr.infixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals("(2.0 - (1.5 / x))", expr.infixString());
    }

    @Test
    @DisplayName("A deeply nested Operation using all UnaryFunctions and operators should evaluate correctly")
    void testDeeplyNestedOperationsWithAllFunctions() throws UnboundVariableException {
        Expression var1 = new Constant(0.0);
        UnaryFunction sinFunc = UnaryFunction.SIN;
        Expression sinExpr = new Constant(sinFunc.apply(var1.eval(MapVarTable.empty())));

        Expression var2 = new Constant(Math.PI / 2);
        UnaryFunction cosFunc = UnaryFunction.COS;
        Expression cosExpr = new Constant(cosFunc.apply(var2.eval(MapVarTable.empty())));

        Expression var3 = new Constant(0.0);
        UnaryFunction tanFunc = UnaryFunction.TAN;
        Expression tanExpr = new Constant(tanFunc.apply(var3.eval(MapVarTable.empty())));

        Expression var4 = new Constant(1.0);
        UnaryFunction sqrtFunc = UnaryFunction.SQRT;
        Expression sqrtExpr = new Constant(sqrtFunc.apply(var4.eval(MapVarTable.empty())));

        Expression var5 = new Constant(3.0);
        UnaryFunction absFunc = UnaryFunction.ABS;
        Expression absExpr = new Constant(absFunc.apply(var5.eval(MapVarTable.empty())));

        Expression var6 = new Constant(0.0);
        UnaryFunction expFunc = UnaryFunction.EXP;
        Expression expExpr = new Constant(expFunc.apply(var6.eval(MapVarTable.empty())));

        Expression var7 = new Constant(1.0);
        UnaryFunction logFunc = UnaryFunction.LOG;
        Expression logExpr = new Constant(logFunc.apply(var7.eval(MapVarTable.empty())));

        // Expression: (sin(0) + cos(π/2)) * (tan(0) + sqrt(1)) + (abs(3) * (exp(0) + log(1)))
        Expression addition1 = new Operation(Operator.ADD, sinExpr, cosExpr);
        Expression addition2 = new Operation(Operator.ADD, tanExpr, sqrtExpr);
        Expression multiplication1 = new Operation(Operator.MULTIPLY, addition1, addition2);

        Expression addition3 = new Operation(Operator.ADD, expExpr, logExpr);
        Expression multiplication2 = new Operation(Operator.MULTIPLY, absExpr, addition3);

        Expression finalExpression = new Operation(Operator.ADD, multiplication1, multiplication2);

        assertEquals(3.0, finalExpression.eval(MapVarTable.empty()));
    }


    @Test
    @DisplayName("An Operation node with leaf operands should produce a postfix representation " +
            "consisting of its first operand, its second operand, and its operator symbol " +
            "separated by spaces")
    void testPostfixLeaves() {
        MapVarTable varTable = MapVarTable.of("x", 2.0);
        Expression var = new Variable("x");
        Expression expr = new Operation(Operator.ADD ,var,new Constant(3.5));
        assertEquals("x 3.5 +", expr.postfixString());
    }

    @Test
    @DisplayName("An Operation node with an Operation for either operand should produce the " +
            "expected postfix representation")
    void testPostfixRecursive() {
        Expression expr = new Operation(Operator.ADD,
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                new Constant(2.0));
        assertEquals("1.5 x * 2.0 +", expr.postfixString());

        expr = new Operation(Operator.SUBTRACT,
                new Constant(2.0),
                new Operation(Operator.DIVIDE, new Constant(1.5), new Variable("x")));
        assertEquals("2.0 1.5 x / -", expr.postfixString());
    }

    @Test
    @DisplayName("A deeply nested Operation should produce the expected postfix representation")
    void testDeeplyNestedOperations() {
        Expression expr = new Operation(Operator.SUBTRACT,
                new Operation(Operator.ADD,
                        new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                        new Operation(Operator.DIVIDE, new Constant(2.0), new Variable("y"))),
                new Operation(Operator.POW, new Constant(3.0), new Variable("z")));

        assertEquals("1.5 x * 2.0 y / + 3.0 z ^ -", expr.postfixString());
    }


    @Test
    @DisplayName("An Operation node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("An Operation node should equal another Operation node with the same " +
            "operator and operands")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        Expression expr2 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("An Operation node should not equal another Operation node with a different " +
            "operator")
    void testEqualsFalse() {
        Expression expr1 = new Operation(Operator.ADD, new Constant(1.5), new Variable("x"));
        Expression expr2 = new Operation(Operator.ADD, new Constant(2.5), new Variable("x"));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("y"));
        assertEquals(2, deps.size());
    }

    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands, one of its operands " +
                 "is Operation node.")
    void testDependenciesWithOp() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Operation(Operator.ADD, new Variable("x"),
                new Variable("z")), new Variable("y"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("y"));
        assertTrue(deps.contains("z"));
        assertEquals(3, deps.size());
    }

    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands, one of its operands " +
            "is Constant.")
    void testDependenciesWithConst() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Constant(6), new Variable("y"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("y"));
        assertEquals(1, deps.size());
    }

    @Test
    @DisplayName("An Operation node depends on the dependencies of both of its operands, one of its operands " +
            "is Constant.")
    void testDependenciesWithCondition() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Operation(Operator.ADD, new Variable("x"),
                new Conditional(new Constant(0),
                        new Variable("y"),
                        new Variable("z")));
        Set <String> deps=expr.dependencies();
        assertTrue(deps.contains("y"));
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("z"));
        assertEquals(3, deps.size());
    }


    @Test
    @DisplayName("An Operation node for ADD with two Constant operands should optimize to a " +
            "Constant containing their sum")
    void testOptimizeAdd() {
        Expression expr = new Operation(Operator.ADD, new Constant(6), new Constant(2));
        Expression opt = expr.optimize(MapVarTable.empty());
        assertEquals(new Constant(8), opt);
    }

    @Test
    @DisplayName("An Operation node for ADD with two Variable operands. Two Variable operands are in the MapVarTable. "
            + "Should optimize to a Constant containing their sum")
    void testOptimizeAddWithVar() {
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Expression opt = expr.optimize(MapVarTable.of("x",1,"y",2));
        assertEquals(new Constant(3), opt);
    }

    @Test
    @DisplayName("An Operation node for ADD with two operands. One of the operands is also Operation")
    void testOptimizeAddWithOpt() {
        Expression expr = new Operation(
                Operator.ADD,
                new Variable("x"),
                new Operation(Operator.ADD, new Constant(1), new Constant(2))
        );
        Expression opt = expr.optimize(MapVarTable.of("x",1));
        assertEquals(new Constant(4), opt);
    }

    @Test
    @DisplayName("An Operation node for ADD with two operands. One of the operands is Conditional type.")
    void testOptimizeAddWithCondition() {
        Expression expr = new Operation(
                Operator.ADD,
                new Variable("x"),
                new Conditional(new Constant(0), new Constant(1),new Constant(2))
        );
        Expression opt = expr.optimize(MapVarTable.of("x",1));
        assertEquals(new Constant(3), opt);
    }

    @Test
    @DisplayName("An Operation node for ADD with two Variable operands. One of the two operands are not in the " +
            "MapVarTable. Will not be optimized.")
    void testNoOptimize1() {
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Expression opt = expr.optimize(MapVarTable.of("y",2));
        assertEquals(expr, opt);
    }

    @DisplayName("An Operation node for ADD with two Variable operands. Both operands are not in the " +
            "MapVarTable. Will not be optimized.")
    void testNoOptimize2() {
        Expression expr = new Operation(Operator.ADD, new Variable("x"), new Variable("y"));
        Expression opt = expr.optimize(MapVarTable.empty());
        assertEquals(expr, opt);
    }
}


class ConditionalExpressionTest {

    @Test
    @DisplayName("A Conditional node with Constant non-zero condition and Constant branches should "
            + "evaluate to the first branch's value.")
    void testEvalConstTrue() throws UnboundVariableException {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Conditional(new Constant(1), new Constant(2), new Constant(5));
        assertEquals(2.0, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("A Conditional node with Constant zero condition and Constant branches should "
            + "evaluate to the second branch's value.")
    void testEvalConstFalse() throws UnboundVariableException {
        Expression expr = new Conditional(new Constant(0), new Constant(2), new Constant(5));
        assertEquals(5.0, expr.eval(MapVarTable.empty()));
    }

    @Test
    @DisplayName("A Conditional node with a Variable condition should evaluate to the appropriate "
            + "branch when the variable has a zero or non-zero value in the var map")
    void testEvalCondBound() throws UnboundVariableException {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Conditional(new Variable("x"), new Constant(2), new Constant(5));
        assertEquals(2.0, expr.eval(MapVarTable.of("x", 1.0)));

        MapVarTable varTable = MapVarTable.of("x", 0.0);
        Expression var = new Variable("x");
        expr = new Conditional(var, new Constant(2), new Constant(5));
        assertEquals(2.0, expr.eval(MapVarTable.of("x", 1.0)));
    }

    @Test
    @DisplayName("A Conditional node with a Variable condition should throw an UnboundVariableException" +
            " when evaluated if the variable is not in the var map")
    void testEvalCondUnbound() {
        try {
            Expression expr = new Conditional(new Variable("x"), new Constant(2), new Constant(5));
            expr.eval(MapVarTable.empty());
        }
        catch (UnboundVariableException e) {
        }
    }


    @Test
    @DisplayName("A Conditional node with leaf condition and branches should report that 1 "
            + "operation is required to evaluate it")
    void testOpCountLeaves() {
        Expression expr = new Conditional(new Constant(0), new Constant(2), new Constant(5));
        assertEquals(1, expr.opCount());
    }


    @Test
    @DisplayName("A Conditional node with non-leaf condition and branches with different op counts "
            + "should report the correct number of operations to evaluate it regardless of which "
            + "branch is more expensive")
    void testOpCountRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        //  True branch is more expensive
        Expression expr1 = new Conditional(
                new Operation(Operator.ADD, new Variable("x"), new Constant(3)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Constant(7));
        assertEquals(3, expr1.opCount());

        // False branch is more expensive
        Expression expr2 = new Conditional(
                new Operation(Operator.SUBTRACT, new Variable("x"), new Constant(1)),
                new Operation(Operator.MULTIPLY, new Constant(1.5), new Variable("x")),
                expr1);
        assertEquals(5, expr2.opCount());
    }


    @Test
    @DisplayName("A Conditional node with leaf condition and branches should produce an infix "
            + "representation consisting of its condition, the '?' symbol surrounded by spaces, "
            + "its true branch, the ':' symbol surrounded by spaces, and its false branch, all "
            + "enclosed in parentheses")
    void testInfixLeaves() {
        Expression expr1 = new Conditional(new Constant(1.0), new Constant(2.0), new Constant(5.0));
        assertEquals("(1.0 ? 2.0 : 5.0)", expr1.infixString());

        Expression expr2 = new Conditional(new Variable("x"), new Constant(2.0), new Constant(5.0));
        assertEquals("(x ? 2.0 : 5.0)", expr2.infixString());
    }

    @Test
    @DisplayName("A Conditional node with Operation condition and branches should produce the " +
            "expected infix representation.")
    void testInfixRecursive() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Conditional(
                new Operation(Operator.ADD, new Variable("x"), new Constant(3)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Constant(7));
        assertEquals("((x + 3.0) ? (2.0 * y) : 7.0)", expr1.infixString());

        UnaryFunction sinFunction = UnaryFunction.SIN;
        Expression sinExpr = new Constant(sinFunction.apply(0.0));
        Expression expr2 = new Conditional(sinExpr, new Constant(10.0), new Constant(20.0));
        assertEquals("(0.0 ? 10.0 : 20.0)", expr2.infixString());

        Expression operationExpr = new Operation(Operator.MULTIPLY, new Constant(2.0), new Variable("x"));
        Expression expr3 = new Conditional(operationExpr, new Constant(3.0), new Constant(6.0));
        assertEquals("((2.0 * x) ? 3.0 : 6.0)", expr3.infixString());

        UnaryFunction cosFunction = UnaryFunction.COS;
        Expression cosExpr = new Constant(cosFunction.apply(Math.PI));
        Expression trueBranchExpr = new Operation(Operator.ADD, new Constant(2.0), new Variable("y"));
        Expression falseBranchExpr = new Operation(Operator.SUBTRACT, new Constant(5.0), new Variable("z"));
        Expression expr4 = new Conditional(cosExpr, trueBranchExpr, falseBranchExpr);
        assertEquals("(-1.0 ? (2.0 + y) : (5.0 - z))", expr4.infixString());
    }


    @Test
    @DisplayName("A Condition node with leaf condition and branches should produce a postfix "
            + "representation consisting of its condition, its true branch, its false branch, and "
            + "the '?:' symbol, separated by spaces")
    void testPostfixLeaves() {
        Expression expr1 = new Conditional(new Constant(1.0), new Constant(2.0), new Constant(5.0));
        assertEquals("1.0 2.0 5.0 ?:", expr1.postfixString());

        Expression expr2 = new Conditional(new Variable("x"), new Constant(2.0), new Constant(5.0));
        assertEquals("x 2.0 5.0 ?:", expr2.postfixString());
    }

    @Test
    @DisplayName("A Conditional node with Operation condition and branches should produce the "
            + "expected postfix representation")
    void testPostfixRecursive() {
        Expression innerConditional = new Conditional(new Constant(1.0),
                new Constant(10.0), new Constant(20.0));
        Expression expr1 = new Conditional(new Constant(0.0), innerConditional, new Constant(5.0));
        assertEquals("0.0 1.0 10.0 20.0 ?: 5.0 ?:", expr1.postfixString());

        UnaryFunction sinFunction = UnaryFunction.SIN;
        Expression sinExpr = new Constant(sinFunction.apply(0.0));
        Expression expr2 = new Conditional(sinExpr, new Constant(10.0), new Constant(20.0));
        assertEquals("0.0 10.0 20.0 ?:", expr2.postfixString());

        UnaryFunction cosFunction = UnaryFunction.COS;
        Expression cosExpr = new Constant(cosFunction.apply(Math.PI));  // cos(π) = -1
        Expression trueBranchExpr = new Operation(Operator.ADD, new Constant(2.0), new Variable("y"));
        Expression falseBranchExpr = new Operation(Operator.SUBTRACT, new Constant(5.0), new Variable("z"));
        Expression expr3 = new Conditional(cosExpr, trueBranchExpr, falseBranchExpr);
        assertEquals("-1.0 2.0 y + 5.0 z - ?:", expr3.postfixString());

        Expression operationExpr = new Operation(Operator.MULTIPLY, new Constant(2.0), new Variable("x"));
        Expression expr4 = new Conditional(operationExpr, new Constant(3.0), new Constant(6.0));
        assertEquals("2.0 x * 3.0 6.0 ?:", expr4.postfixString());
    }

    @Test
    @DisplayName("A Condition node should equal itself")
    void testEqualsSelf() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Conditional(new Variable("x"), new Constant(2), new Constant(5));
        assertTrue(expr.equals(expr));
    }

    @Test
    @DisplayName("A Condition node should equal another Condition node with the same " +
            "condition and branches")
    void testEqualsTrue() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr1 = new Conditional(
                new Operation(Operator.ADD, new Variable("x"), new Constant(3)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Constant(7));
        Expression expr2 = new Conditional(
                new Operation(Operator.ADD, new Variable("x"), new Constant(3)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Constant(7));
        assertTrue(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Condition node should not equal another Condition node with a different " +
            "condition")
    void testEqualsFalseCondition() {
        Expression expr1 = new Conditional(new Constant(0),new Constant(1),new Constant(3));
        Expression expr2 = new Conditional(new Constant(1),new Constant(1),new Constant(3));

        assertFalse(expr1.equals(expr2));
    }

    @Test
    @DisplayName("A Condition node should not equal another Condition node with a different " +
            "branch")
    void testEqualsFalseBranch() {
        Expression expr1 = new Conditional(new Constant(0),new Constant(1),new Constant(2));
        Expression expr2 = new Conditional(new Constant(0),new Constant(1),new Constant(3));
        assertFalse(expr1.equals(expr2));
    }


    @Test
    @DisplayName("A Condition node depends on the dependencies of its condition and both of its "
            + "branches")
    void testDependencies() {
        // TODO: Uncomment this test, adjusting constructor invocations as necessary
        Expression expr = new Conditional(
                new Operation(Operator.ADD, new Variable("x"), new Constant(3)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Variable("z"));
        Set<String> deps = expr.dependencies();
        assertTrue(deps.contains("x"));
        assertTrue(deps.contains("y"));
        assertTrue(deps.contains("z"));
        assertEquals(3, deps.size());
    }


    @Test
    @DisplayName("A Condition node with Constant condition should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeConstCondition() {
        Expression expr = new Conditional(
                new Constant(1),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Variable("z"));
        MapVarTable varTable = MapVarTable.of("y", 2.0);
        assertEquals(expr.optimize(varTable),new Constant(4));
    }

    @Test
    @DisplayName("A Condition node with Expression condition should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeExprCondition() {
        Expression expr = new Conditional(
                new Operation(Operator.MULTIPLY, new Constant(1), new Constant(0)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Variable("y")),
                new Variable("y"));
        MapVarTable varTable = MapVarTable.of("y", 2.0);
        assertEquals(expr.optimize(varTable),new Constant(2));
    }

    @Test
    @DisplayName("A Condition node with Constant branches should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeConstBranch() {
        Expression expr = new Conditional(
                new Operation(Operator.MULTIPLY, new Constant(1), new Constant(0)),
                new Constant(1),
                new Constant(2));
        MapVarTable varTable = MapVarTable.empty();
        assertEquals(expr.optimize(varTable),new Constant(2));
    }

    @Test
    @DisplayName("A Condition node with Variable branches should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeVarBranch() {
        Expression expr = new Conditional(
                new Operation(Operator.MULTIPLY, new Constant(1), new Constant(0)),
                new Variable("x"),
                new Variable("y"));
        MapVarTable varTable = MapVarTable.of("y",2);
        assertEquals(expr.optimize(varTable),new Constant(2));
    }

    @Test
    @DisplayName("A Condition node with Operation branches should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeOpBranch() {
        Expression expr = new Conditional(
                new Operation(Operator.MULTIPLY, new Constant(1), new Constant(0)),
                new Operation(Operator.ADD, new Constant(1), new Constant(0)),
                new Operation(Operator.MULTIPLY, new Constant(2), new Constant(3)));
        MapVarTable varTable = MapVarTable.empty();
        assertEquals(expr.optimize(varTable),new Constant(6));
    }

    @Test
    @DisplayName("A Condition node with Conditional branches should optimize to its appropriate "
            + "optimized branch")
    void testOptimizeConditionBranch() {
        Expression expr = new Conditional(
                new Operation(Operator.MULTIPLY, new Constant(1), new Constant(0)),
                new Conditional(new Constant(1), new Constant(2), new Constant(3)),
                new Conditional(new Constant(0), new Constant(5), new Constant(6)));
        MapVarTable varTable = MapVarTable.empty();
        assertEquals(expr.optimize(varTable),new Constant(6));
    }

}
