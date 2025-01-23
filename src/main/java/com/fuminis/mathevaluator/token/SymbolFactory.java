package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public abstract class SymbolFactory implements Token, TokenFactory {
    private final char charOperator;
    private final int precedence;
    private final boolean nextTokenIsOperatorOrStart;
    private final boolean prevTokenIsOperatorOrStart;
    private final int nbOperands;

    public SymbolFactory(char charOperator, int precedence, boolean nextTokenIsOperatorOrStart, boolean prevTokenIsOperatorOrStart, int nbOperands) {
        this.charOperator = charOperator;
        this.precedence = precedence;
        this.nextTokenIsOperatorOrStart = nextTokenIsOperatorOrStart;
        this.prevTokenIsOperatorOrStart = prevTokenIsOperatorOrStart;
        this.nbOperands = nbOperands;
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[char=" + charOperator + "]";
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

    @Override
    public int nbOperands() {
        return this.nbOperands;
    }

    public Token getToken(Stack<Character> chars) {
        chars.pop();
        return this;
    }
}
