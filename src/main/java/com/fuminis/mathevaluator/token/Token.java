package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Token {
    default int precedence() {
        return 0;
    }

    Expr getExpr(Stack<Expr> operands);

    void toExpression(Stack<Expr> operands, Stack<Token> operators);
}
