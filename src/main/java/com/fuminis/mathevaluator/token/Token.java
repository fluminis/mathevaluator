package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public interface Token {
    default int precedence() {
        return 0;
    }

    default int nbOperands() {
        return 2;
    }

    Expr getExpr(Stack<Expr> operands);

    void toExpression(Stack<Expr> operands, Stack<Token> operators);

    static void getExpr(Stack<Expr> operands, Stack<Token> operators) {
        Token token = operators.pop();
        if (token.nbOperands() > operands.size()) {
            throw new MathEvaluationException("wrong number of operands for " + token);
        }
        Expr expr = token.getExpr(operands);
        if (expr != null) {
            operands.push(expr);
        }
    }
}
