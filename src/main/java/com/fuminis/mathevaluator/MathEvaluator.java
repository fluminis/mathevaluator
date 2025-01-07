package com.fuminis.mathevaluator;

import com.fuminis.mathevaluator.expr.Expr;
import com.fuminis.mathevaluator.expr.Number;
import com.fuminis.mathevaluator.token.Addition;
import com.fuminis.mathevaluator.token.CloseParenthesis;
import com.fuminis.mathevaluator.token.Division;
import com.fuminis.mathevaluator.token.Multiplication;
import com.fuminis.mathevaluator.token.Negative;
import com.fuminis.mathevaluator.token.NumberFactory;
import com.fuminis.mathevaluator.token.OpenParenthesis;
import com.fuminis.mathevaluator.token.OperatorFactory;
import com.fuminis.mathevaluator.token.Pow;
import com.fuminis.mathevaluator.token.Subtraction;
import com.fuminis.mathevaluator.token.Token;
import com.fuminis.mathevaluator.token.TokenFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MathEvaluator {

    private List<TokenFactory> tokenFactories = new ArrayList<>();

    public MathEvaluator() {
        this.tokenFactories = List.of(
                new NumberFactory(),
                new Addition(),
                new Subtraction(),
                new Negative(),
                new Multiplication(),
                new Division(),
                new Pow(),
                new OpenParenthesis(),
                new CloseParenthesis()
        );
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
        Stack<OperatorFactory> operators = new Stack<>();
        for (Token token : tokens) {
            if (token instanceof Number number) {
                operands.push(number);
            } else if (token instanceof OperatorFactory operator) {
                if (operator.charOperator() == ')') {
                    while (operators.peek().charOperator() != '(') {
                        operands.push(operators.pop().getExpr(operands));
                    }
                    operators.pop();
                } else {
                    if (!operators.empty() && operators.peek().precedence() >= operator.precedence() && operator.charOperator() != '(') {
                        operands.push(operators.pop().getExpr(operands));
                    }
                    operators.push(operator);
                }
            }
        }
        while (!operators.empty()) {
            operands.push(operators.pop().getExpr(operands));
        }
        return operands.pop();
    }

}
