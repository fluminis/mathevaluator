package com.fuminis.mathevaluator.operator;

import com.fuminis.mathevaluator.expr.Expr;
import com.fuminis.mathevaluator.token.Token;

import java.util.Stack;
import java.util.function.Function;

public class OperatorFactory implements Token {
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

    public boolean support(char aChar, boolean prevTokenIsOperatorOrStart) {
        return charOperator == aChar && this.prevTokenIsOperatorOrStart == prevTokenIsOperatorOrStart;
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
}
