package cs2110;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Utilities for parsing RPN expressions.
 */
public class RpnParser {

    /**
     * Parse the RPN expression in `exprString` and return the corresponding expression tree. Tokens
     * must be separated by whitespace.  Valid tokens include decimal numbers (scientific notation
     * allowed), arithmetic operators (+, -, *, /, ^), the conditional operator (:?), function names
     * (with the suffix "()"), and variable names (anything else).  When a function name is
     * encountered, the corresponding function will be retrieved from `funcDefs` using the name
     * (without "()" suffix) as the key.
     *
     * @throws IncompleteRpnException     if the expression has too few or too many operands
     *                                    relative to operators and functions.
     * @throws UndefinedFunctionException if a function name applied in `exprString` is not present
     *                                    in `funcDefs`.
     */
    public static Expression parse(String exprString, Map<String, UnaryFunction> funcDefs)
            throws IncompleteRpnException, UndefinedFunctionException {
        assert exprString != null;
        assert funcDefs != null;

        // Each token will result in a subexpression being pushed onto this stack.  If the
        // subexpression requires arguments, they are first popped off of this stack.
        Deque<Expression> stack = new ArrayDeque<>();

        if (exprString.isEmpty()) {
            throw new IncompleteRpnException(exprString, stack.size());
        }

        // Loop over each token in the expression string from left to right
        for (Token token : Token.tokenizer(exprString)) {
            // TODO: Based on the dynamic type of the token, create the appropriate Expression node
            //  and push it onto the stack, popping arguments as needed.
            //  The "number" token is done for you as an example.
            //  __DONE__
            if (token instanceof Token.Number) {
                Token.Number numToken = (Token.Number) token;
                stack.push(new Constant(numToken.doubleValue()));
            }

            if (token instanceof Token.Operator) {
                Token.Operator operatorToken = (Token.Operator) token;
                try {
                    Expression right = stack.pop();
                    Expression left = stack.pop();
                    Expression Operation = new Operation(operatorToken.opValue(), left, right);

                    stack.push(Operation);
                } catch (NoSuchElementException e) {
                    throw new IncompleteRpnException(exprString, stack.size());
                }
            }

            if (token instanceof Token.Variable) {
                Token.Variable variableToken = (Token.Variable) token;
                stack.push(new Variable(variableToken.value()));
            }

            if (token instanceof Token.CondOp) {
                try {
                    Expression conditionBranch = stack.pop();
                    Expression trueBranch = stack.pop();
                    Expression falseBranch = stack.pop();

                    Expression Conditional = new Conditional(conditionBranch, trueBranch, falseBranch);

                    stack.push(Conditional);
                } catch (NoSuchElementException e) {
                    throw new IncompleteRpnException(exprString, stack.size());
                }
            }

            if (token instanceof Token.Function) {
                try {
                    Token.Function functionToken = (Token.Function) token;
                    UnaryFunction func = funcDefs.get(functionToken.name());
                    Expression input = stack.pop();
                    Expression function = new Application(func, input);

                    stack.push(function);
                } catch (AssertionError e) {
                    throw new UndefinedFunctionException(funcDefs.toString());
                }
            }
        }
        // TODO: Return the overall expression node.  (This might also be a good place to check that
        //  the string really did correspond to a single expression.)
        //  __DONE__

        if (stack.size() == 1) {
            return stack.pop();
        } else {
            throw new IncompleteRpnException(exprString, stack.size());
        }
    }
}
