package com.fuminis.mathevaluator.operator;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Subtraction extends OperatorFactory {
    public Subtraction() {
        super('-', 1, true, false, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        Expr right = operands.pop();
        Expr left = operands.pop();
        return () -> left.evaluate() - right.evaluate();
    }
}
