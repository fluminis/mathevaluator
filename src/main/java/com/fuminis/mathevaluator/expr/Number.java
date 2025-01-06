package com.fuminis.mathevaluator.expr;

import com.fuminis.mathevaluator.token.Token;

public record Number(double number) implements Expr, Token {
    @Override
    public double evaluate() {
        return number;
    }
}
