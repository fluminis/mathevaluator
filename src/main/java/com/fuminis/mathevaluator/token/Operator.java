package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Operator {
    int precedence();

    int nbOperands();

    Expr getExpr(Stack<Expr> operands);

    default void assertNbOperand(Stack<Expr> operands) {
        if (nbOperands() > operands.size()) {
            throw new MathEvaluationException("wrong number of operands for " + this);
        }
    }
}
