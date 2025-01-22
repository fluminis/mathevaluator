package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Pow extends OperatorFactory {
    public Pow() {
        super('^', 3, true, false, 2);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        Expr right = operands.pop();
        Expr left = operands.pop();
        return () -> Math.pow(left.evaluate(), right.evaluate());
    }
}
