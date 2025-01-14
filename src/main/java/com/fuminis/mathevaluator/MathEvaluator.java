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

    private static List<TokenFactory> DEFAULT_FACTORIES = List.of(
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

    private VariableFactory variableFactory;
    private FunctionFactory functionFactory;
    private final List<TokenFactory> tokenFactories;

    public MathEvaluator() {
        this.tokenFactories = new ArrayList<>(DEFAULT_FACTORIES);
    }

    public MathEvaluator setVariables(Map<String, Double> variables) {
        if (variableFactory == null) {
            variableFactory = new VariableFactory();
            tokenFactories.add(variableFactory);
        }
        variableFactory.setVariables(variables);
        return this;
    }

    public MathEvaluator setFunctions(Map<String, IMathFunction> functions) {
        if (functionFactory == null) {
            functionFactory = new FunctionFactory();
            tokenFactories.add(functionFactory);
        }
        functionFactory.setFunctions(functions);
        return this;
    }

    public double calculate(String expression) {
        List<Token> tokens = tokenize(expression);
        Expr expr = toExpression(tokens);
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
