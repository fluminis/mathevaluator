package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class Coma extends SymbolFactory {
    public Coma() {
        super(',', false, false);
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
        while (!operators.empty() && !(operators.peek() instanceof OpenParenthesis)) {
            operands.push(operators.pop().getExpr(operands));
        }
        // do not remove open parenthesis
    }
}
