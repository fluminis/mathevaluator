package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;
import java.util.function.Function;

public abstract class OperatorFactory implements Token, TokenFactory {
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

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
        if (!operators.empty() && operators.peek().precedence() >= this.precedence()) {
            operands.push(operators.pop().getExpr(operands));
        }
        operators.push(this);
    }

    public Expr getExpr(Stack<Expr> operands) {
        return generator.apply(operands);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[char=" + charOperator + ", precedence=" + precedence + "]";
    }

    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        return charOperator == chars.peek() && this.prevTokenIsOperatorOrStart == prevTokenIsOperatorOrStart;
    }

    public boolean nextTokenIsOperatorOrStart() {
        return nextTokenIsOperatorOrStart;
    }

    public int precedence() {
        return precedence;
    }

    public char charOperator() {
        return charOperator;
    }

    public Token getToken(Stack<Character> chars) {
        chars.pop();
        return this;
    }
}
