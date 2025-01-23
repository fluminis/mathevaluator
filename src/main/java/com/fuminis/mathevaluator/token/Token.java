package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Token {
    void toExpression(Stack<Expr> operands, Stack<Operator> operators);
}
