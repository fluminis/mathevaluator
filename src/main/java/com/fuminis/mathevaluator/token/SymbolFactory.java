package com.fuminis.mathevaluator.token;

import java.util.Stack;

public abstract class SymbolFactory implements Token, TokenFactory {
    private final char charOperator;
    private final boolean nextTokenIsOperatorOrStart;
    private final boolean prevTokenIsOperatorOrStart;

    public SymbolFactory(char charOperator, boolean nextTokenIsOperatorOrStart, boolean prevTokenIsOperatorOrStart) {
        this.charOperator = charOperator;
        this.nextTokenIsOperatorOrStart = nextTokenIsOperatorOrStart;
        this.prevTokenIsOperatorOrStart = prevTokenIsOperatorOrStart;
    }

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        return charOperator == chars.peek() && this.prevTokenIsOperatorOrStart == prevTokenIsOperatorOrStart;
    }

    @Override
    public boolean nextTokenIsOperatorOrStart() {
        return nextTokenIsOperatorOrStart;
    }

    @Override
    public Token getToken(Stack<Character> chars) {
        chars.pop();
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[char=" + charOperator + "]";
    }
}
