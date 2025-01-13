package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class CloseParenthesis extends OperatorFactory {
    public CloseParenthesis() {
        super(')', 4, false, false, null);
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
        operators.pop(); // remove open parenthesis
        if (!operators.isEmpty() && operators.peek() instanceof FunctionFactory.CustomFunction) {
            operands.push(operators.pop().getExpr(operands));
        }
    }
}
