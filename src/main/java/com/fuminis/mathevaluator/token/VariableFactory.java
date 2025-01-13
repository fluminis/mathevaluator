package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Map;
import java.util.Stack;

public class VariableFactory implements TokenFactory {

    private Map<String, Double> variables;

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        int index = chars.size() - 1;
        if (Character.isAlphabetic(chars.get(index))) {
            while (index >= 0 && (Character.isAlphabetic(chars.get(index)) || Character.isDigit(chars.get(index)) || chars.get(index) == '_')) {
                index--;
            }
            return index == 0 || chars.get(index) != '(';
        }
        return false;
    }

    public Token getToken(Stack<Character> chars) {
        String currentToken = "";
        do {
            currentToken += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        String varName = currentToken;
        return new Number(variables.get(varName));
    }

    public boolean nextTokenIsOperatorOrStart() {
        return false;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables = variables;
    }

    record Number(double evaluate) implements Expr, Token {
        @Override
        public Expr getExpr(Stack<Expr> operands) {
            return this;
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
            operands.push(this);
        }
    }
}
