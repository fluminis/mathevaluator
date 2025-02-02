package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class CloseParenthesis extends SymbolFactory {
    public CloseParenthesis() {
        super(')', false, false);
    }

    @Override
    public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
        while (!operators.empty() && !(operators.peek() instanceof OpenParenthesis)) {
            operands.push(operators.pop().getExpr(operands));
        }
        if (operators.empty() || !(operators.peek() instanceof OpenParenthesis)) {
            throw new MathEvaluationException("a close parenthesis does not have corresponding open parenthesis");
        }
        operators.pop(); // remove open parenthesis
    }
}
