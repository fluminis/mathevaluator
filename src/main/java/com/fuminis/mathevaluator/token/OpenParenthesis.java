package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class OpenParenthesis extends SymbolFactory implements Operator {
    public OpenParenthesis() {
        super('(', true, true);
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
        operators.push(this);
    }

    @Override
    public int precedence() {
        return 0;
    }

    @Override
    public int nbOperands() {
        return 0;
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }
}
