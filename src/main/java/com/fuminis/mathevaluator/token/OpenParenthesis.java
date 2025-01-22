package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class OpenParenthesis extends OperatorFactory {
    public OpenParenthesis() {
        super('(', -1, true, true, 2);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
        operators.push(this);
    }
}
