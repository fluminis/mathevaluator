package com.fuminis.mathevaluator.operator;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class CloseParenthesis extends OperatorFactory {
    public CloseParenthesis() {
        super(')', 4, false, false, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }
}
