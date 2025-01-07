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
    public void toExpression(Stack<Expr> operands, Stack<OperatorFactory> operators) {
        while (operators.peek().charOperator() != '(') {
            operands.push(operators.pop().getExpr(operands));
        }
        operators.pop();
    }
}
