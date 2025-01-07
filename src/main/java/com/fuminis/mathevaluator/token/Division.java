package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Division extends OperatorFactory {
    public Division() {
        super('/', 2, true, false, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        Expr right = operands.pop();
        Expr left = operands.pop();
        return () -> left.evaluate() / right.evaluate();
    }
}
