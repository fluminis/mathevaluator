package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class OpenParenthesis extends OperatorFactory {
    public OpenParenthesis() {
        super('(', -1, true, true, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }
}
