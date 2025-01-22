package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Coma extends OperatorFactory {
    public Coma() {
        super(',', 4, false, false, 2);
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        return null;
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
        while (!operators.empty() && !(operators.peek() instanceof OpenParenthesis)) {
            Token.getExpr(operands, operators);
        }
        // do not remove open parenthesis
    }
}
