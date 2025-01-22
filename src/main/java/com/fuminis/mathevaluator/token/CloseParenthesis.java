package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class CloseParenthesis extends OperatorFactory {
    public CloseParenthesis() {
        super(')', 4, false, false, 2);
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
        if (operators.empty() || !(operators.peek() instanceof OpenParenthesis)) {
            throw new MathEvaluationException("a close parenthesis does not have corresponding open parenthesis");
        }
        operators.pop(); // remove open parenthesis

    }
}
