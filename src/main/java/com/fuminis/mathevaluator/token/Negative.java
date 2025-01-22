package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Negative extends OperatorFactory {
    public Negative() {
        super('-', 5, true, true, 1);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        Expr expr = operands.pop();
        return () -> - expr.evaluate();
    }
}
