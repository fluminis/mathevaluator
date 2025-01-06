package com.fuminis.mathevaluator.operator;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Pow extends OperatorFactory {
    public Pow() {
        super('^', 3, true, false, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        Expr right = operands.pop();
        Expr left = operands.pop();
        return () -> Math.pow(left.evaluate(), right.evaluate());
    }
}
