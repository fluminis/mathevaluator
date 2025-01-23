package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Stack;

public class NumberFactory implements TokenFactory {

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        return Character.isDigit(chars.peek()) || chars.peek() == '.';
    }

    @Override
    public Token getToken(Stack<Character> chars) {
        String currentToken = "";
        do {
            currentToken += chars.pop();
        } while (!chars.empty() && (Character.isDigit(chars.peek()) || chars.peek() == '.'));
        double number = Double.parseDouble(currentToken);
        return new Number(number);
    }

    @Override
    public boolean nextTokenIsOperatorOrStart() {
        return false;
    }

    record Number(double evaluate) implements Expr, Token {

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
            operands.push(this);
        }
    }
}
