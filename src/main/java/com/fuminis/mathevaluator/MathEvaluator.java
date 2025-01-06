package com.fuminis.mathevaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class MathEvaluator {

    private List<OperatorFactory> operatorFactories =new ArrayList<>();

    public MathEvaluator() {
        this.operatorFactories = List.of(
                new OperatorFactory('+', 1, true, false, operands -> new Addition(operands.pop(), operands.pop())),
                new OperatorFactory('-', 1, true, false, operands -> new Subtraction(operands.pop(), operands.pop())),
                new OperatorFactory('-', 5, true, true, operands -> new Negative(operands.pop())),
                new OperatorFactory('*', 2, true, false, operands -> new Multiplication(operands.pop(), operands.pop())),
                new OperatorFactory('/', 2, true, false, operands -> new Division(operands.pop(), operands.pop())),
                new OperatorFactory('^', 3, true, false, operands -> new Pow(operands.pop(), operands.pop())),
                new OperatorFactory('(', -1, true, true, operands -> null),
                new OperatorFactory(')', 4, false, false, operands -> null)
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
                if (operatorFactory.charOperator == chars[i] && operatorFactory.prevTokenIsOperatorOrStart == prevTokenIsOperatorOrStart) {
                    tokens.add(operatorFactory);
                    prevTokenIsOperatorOrStart = operatorFactory.nextTokenIsOperatorOrStart;
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
                    if (operator.charOperator == ')') {
                        while (operators.peek().charOperator != '(') {
                            operands.push(operators.pop().getExpr(operands));
                        }
                        operators.pop();
                    } else {
                        if (operators.peek().precedence >= operator.precedence && operator.charOperator != '(') {
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

    interface Token {
    }

    public interface Expr {
        double evaluate();
    }

    record Number(double number) implements Expr, Token {
        @Override
        public double evaluate() {
            return number;
        }
    }

    record Addition(Expr right, Expr left) implements Expr {
        @Override
        public double evaluate() {
            return left.evaluate() + right.evaluate();
        }
    }

    record Subtraction(Expr right, Expr left) implements Expr {
        @Override
        public double evaluate() {
            return left.evaluate() - right.evaluate();
        }
    }

    record Multiplication(Expr right, Expr left) implements Expr {
        @Override
        public double evaluate() {
            return left.evaluate() * right.evaluate();
        }
    }

    record Division(Expr right, Expr left) implements Expr {
        @Override
        public double evaluate() {
            return left.evaluate() / right.evaluate();
        }
    }

    record Pow(Expr right, Expr left) implements Expr {
        @Override
        public double evaluate() {
            return Math.pow(left.evaluate(), right.evaluate());
        }
    }

    record Negative(Expr expr) implements Expr {
        @Override
        public double evaluate() {
            return -expr.evaluate();
        }
    }

    public static class OperatorFactory implements Token {
        private final char charOperator;
        private final int precedence;
        private final boolean nextTokenIsOperatorOrStart;
        private final boolean prevTokenIsOperatorOrStart;
        private final Function<Stack<Expr>, Expr> generator;

        public OperatorFactory(char charOperator, int precedence, boolean nextTokenIsOperatorOrStart, boolean prevTokenIsOperatorOrStart, Function<Stack<Expr>, Expr> generator) {
            this.charOperator = charOperator;
            this.precedence = precedence;
            this.nextTokenIsOperatorOrStart = nextTokenIsOperatorOrStart;
            this.prevTokenIsOperatorOrStart = prevTokenIsOperatorOrStart;
            this.generator = generator;
        }

        public Expr getExpr(Stack<Expr> operands) {
            return generator.apply(operands);
        }

        @Override
        public String toString() {
            return charOperator + "(" + precedence + ")";
        }
    }
}
