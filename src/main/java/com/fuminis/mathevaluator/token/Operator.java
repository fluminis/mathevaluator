package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Operator {
    int precedence();

    int nbOperands();

    Expr getExpr(Stack<Expr> operands);
}
