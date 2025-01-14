package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Coma extends OperatorFactory {
    public Coma() {
        super(',', 4, false, false, null);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
        while (!(operators.peek() instanceof OpenParenthesis)) {
            operands.push(operators.pop().getExpr(operands));
        }
        // do not remove open parenthesis
    }
}
