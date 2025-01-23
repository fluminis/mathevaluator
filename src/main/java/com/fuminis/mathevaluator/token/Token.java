package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Token {

    void toExpression(Stack<Expr> operands, Stack<Operator> operators);

    static void getExpr(Stack<Expr> operands, Stack<Operator> operators) {
        Operator operator = operators.pop();
        if (operator.nbOperands() > operands.size()) {
            throw new MathEvaluationException("wrong number of operands for " + operator);
        }
        operands.push(operator.getExpr(operands));
    }
}
