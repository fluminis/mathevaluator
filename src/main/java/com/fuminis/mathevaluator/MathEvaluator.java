package com.fuminis.mathevaluator;

import com.fuminis.mathevaluator.expr.Expr;
import com.fuminis.mathevaluator.token.CloseParenthesis;
import com.fuminis.mathevaluator.token.Coma;
import com.fuminis.mathevaluator.token.FunctionFactory;
import com.fuminis.mathevaluator.token.MathFunction;
import com.fuminis.mathevaluator.token.NumberFactory;
import com.fuminis.mathevaluator.token.OpenParenthesis;
import com.fuminis.mathevaluator.token.Operator;
import com.fuminis.mathevaluator.token.OperatorFactory;
import com.fuminis.mathevaluator.token.Token;
import com.fuminis.mathevaluator.token.TokenFactory;
import com.fuminis.mathevaluator.token.VariableFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class MathEvaluator {

    public static MathEvaluator of(final String expression) {
        return new MathEvaluator(expression);
    }

    private static final List<TokenFactory> DEFAULT_FACTORIES = List.of(
            new NumberFactory(),
            new OperatorFactory(List.of(
                    OperatorFactory.operator('+', 1, false, Double::sum),
                    OperatorFactory.operator('-', 1, false, (a, b) -> a - b),
                    OperatorFactory.operator('-', 5, true, a -> -a),
                    OperatorFactory.operator('/', 2, false, (a, b) -> a / b),
                    OperatorFactory.operator('*', 2, false, (a, b) -> a * b),
                    OperatorFactory.operator('^', 3, false, Math::pow)
            )),
            new OpenParenthesis(),
            new CloseParenthesis(),
            new Coma()
    );

    private final VariableFactory variableFactory;
    private final FunctionFactory functionFactory;
    private final List<TokenFactory> tokenFactories;
    private final String expression;
    private Expr expr;

    private MathEvaluator(String expression) {
        this.expression = expression;
        this.variableFactory = new VariableFactory();
        this.functionFactory = new FunctionFactory();

        this.tokenFactories = new ArrayList<>(DEFAULT_FACTORIES);
        tokenFactories.add(variableFactory);
        tokenFactories.add(functionFactory);
    }

    public MathEvaluator setVariables(Map<String, Double> variables) {
        variableFactory.setVariables(variables);
        return this;
    }

    public MathEvaluator addVariable(String varName, double value) {
        variableFactory.addVariable(varName, value);
        return this;
    }

    public MathEvaluator setFunctions(Map<String, MathFunction> functions) {
        functionFactory.setFunctions(functions);
        return this;
    }

    public MathEvaluator addFunction(String funcName, MathFunction func) {
        functionFactory.addFunction(funcName, func);
        return this;
    }

    private void compile() {
        if (expr == null) {
            this.expr = toExpression(tokenize(this.expression));
        }
    }

    public double calculate() {
        compile();
        return expr.evaluate();
    }

    private List<Token> tokenize(String expression) {
        Stack<Character> chars = getCharacterStack(expression);
        List<Token> tokens = new ArrayList<>();
        boolean prevTokenIsOperatorOrStart = true;
        do {
            if (Character.isWhitespace(chars.peek())) {
                chars.pop();
                continue;
            }
            boolean handled = false;
            for (TokenFactory tokenFactory : tokenFactories) {
                if (tokenFactory.support(chars, prevTokenIsOperatorOrStart)) {
                    handled = true;
                    tokens.add(tokenFactory.getToken(chars));
                    prevTokenIsOperatorOrStart = tokenFactory.nextTokenIsOperatorOrStart();
                    break;
                }
            }
            if (!handled) {
                throw new MathEvaluationException("Illegal token: " + chars.peek() + System.lineSeparator() + formatError(expression, chars.size()));
            }
        } while (!chars.empty());
        return tokens;
    }

    private static Stack<Character> getCharacterStack(String expression) {
        Stack<Character> stack = new Stack<>();
        char[] chars = expression.toCharArray();
        for (int i = chars.length - 1; i >= 0; i--) {
            stack.push(chars[i]);
        }
        return stack;
    }

    private Expr toExpression(List<Token> tokens) {
        Stack<Expr> operands = new Stack<>();
        Stack<Operator> operators = new Stack<>();
        for (Token token : tokens) {
            token.toExpression(operands, operators);
        }
        while (!operators.empty()) {
            operands.push(operators.pop().getExpr(operands));
        }
        return operands.pop();
    }

    private String formatError(String expression, int position) {
        return expression + System.lineSeparator() + " ".repeat(expression.length() - position) + "^";
    }
}
