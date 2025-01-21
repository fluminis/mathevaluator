package com.fuminis.mathevaluator;

import com.fuminis.mathevaluator.expr.Expr;
import com.fuminis.mathevaluator.token.Addition;
import com.fuminis.mathevaluator.token.CloseParenthesis;
import com.fuminis.mathevaluator.token.Coma;
import com.fuminis.mathevaluator.token.Division;
import com.fuminis.mathevaluator.token.FunctionFactory;
import com.fuminis.mathevaluator.token.FunctionFactory.IMathFunction;
import com.fuminis.mathevaluator.token.Multiplication;
import com.fuminis.mathevaluator.token.Negative;
import com.fuminis.mathevaluator.token.NumberFactory;
import com.fuminis.mathevaluator.token.OpenParenthesis;
import com.fuminis.mathevaluator.token.Pow;
import com.fuminis.mathevaluator.token.Subtraction;
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
            new Addition(),
            new Subtraction(),
            new Negative(),
            new Multiplication(),
            new Division(),
            new Pow(),
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

    public MathEvaluator setFunctions(Map<String, IMathFunction> functions) {
        functionFactory.setFunctions(functions);
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
            for (TokenFactory tokenFactory : tokenFactories) {
                if (tokenFactory.support(chars, prevTokenIsOperatorOrStart)) {
                    tokens.add(tokenFactory.getToken(chars));
                    prevTokenIsOperatorOrStart = tokenFactory.nextTokenIsOperatorOrStart();
                    break;
                }
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
        Stack<Token> operators = new Stack<>();
        for (Token token : tokens) {
            token.toExpression(operands, operators);
        }
        while (!operators.empty()) {
            operands.push(operators.pop().getExpr(operands));
        }
        return operands.pop();
    }

}
