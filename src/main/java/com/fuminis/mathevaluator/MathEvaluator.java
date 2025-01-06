package com.fuminis.mathevaluator;

import com.fuminis.mathevaluator.expr.Expr;
import com.fuminis.mathevaluator.expr.Number;
import com.fuminis.mathevaluator.operator.Addition;
import com.fuminis.mathevaluator.operator.CloseParenthesis;
import com.fuminis.mathevaluator.operator.Division;
import com.fuminis.mathevaluator.operator.Multiplication;
import com.fuminis.mathevaluator.operator.Negative;
import com.fuminis.mathevaluator.operator.OpenParenthesis;
import com.fuminis.mathevaluator.operator.OperatorFactory;
import com.fuminis.mathevaluator.operator.Pow;
import com.fuminis.mathevaluator.operator.Subtraction;
import com.fuminis.mathevaluator.token.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MathEvaluator {

    private List<OperatorFactory> operatorFactories =new ArrayList<>();

    public MathEvaluator() {
        this.operatorFactories = List.of(
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
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        boolean prevTokenIsOperatorOrStart = true;
        char[] chars = expression.toCharArray();
        do {
            if (Character.isWhitespace(chars[i])) {
                i++;
                continue;
            }
            if (Character.isDigit(chars[i]) || chars[i] == '.') {
                String currentToken = "";
                do {
                    currentToken += chars[i];
                    i++;
                } while (i < chars.length && (Character.isDigit(chars[i]) || chars[i] == '.'));
                tokens.add(new Number(Double.parseDouble(currentToken)));
                prevTokenIsOperatorOrStart = false;
                continue;
            }
            for (OperatorFactory operatorFactory : operatorFactories) {
                if (operatorFactory.support(chars[i], prevTokenIsOperatorOrStart)) {
                    tokens.add(operatorFactory);
                    prevTokenIsOperatorOrStart = operatorFactory.nextTokenIsOperatorOrStart();
                    break;
                }
            }
            i++;
        } while (i < chars.length);
        return tokens;
    }

    private Expr toExpression(List<Token> tokens) {
        Stack<Expr> operands = new Stack<>();
        Stack<OperatorFactory> operators = new Stack<>();
        for (Token token : tokens) {
            if (token instanceof Number number) {
                operands.push(number);
            } else if (token instanceof OperatorFactory operator) {
                if (!operators.empty()) {
                    if (operator.charOperator() == ')') {
                        while (operators.peek().charOperator() != '(') {
                            operands.push(operators.pop().getExpr(operands));
                        }
                        operators.pop();
                    } else {
                        if (operators.peek().precedence() >= operator.precedence() && operator.charOperator() != '(') {
                            operands.push(operators.pop().getExpr(operands));
                        }
                        operators.push(operator);
                    }
                } else {
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
