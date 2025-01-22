package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public abstract class OperatorFactory implements Token, TokenFactory {
    private final char charOperator;
    private final int precedence;
    private final boolean nextTokenIsOperatorOrStart;
    private final boolean prevTokenIsOperatorOrStart;
    private final int nbOperands;

    public OperatorFactory(char charOperator, int precedence, boolean nextTokenIsOperatorOrStart, boolean prevTokenIsOperatorOrStart, int nbOperands) {
        this.charOperator = charOperator;
        this.precedence = precedence;
        this.nextTokenIsOperatorOrStart = nextTokenIsOperatorOrStart;
        this.prevTokenIsOperatorOrStart = prevTokenIsOperatorOrStart;
        this.nbOperands = nbOperands;
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
        if (!operators.empty() && operators.peek().precedence() >= this.precedence()) {
            Token.getExpr(operands, operators);
        }
        operators.push(this);
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
