package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

public class FunctionFactory implements TokenFactory {

    private Map<String, Function<Double, Double>> functions;

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        int index = chars.size() - 1;
        if (Character.isAlphabetic(chars.get(index))) {
            while (index >= 0 && (Character.isAlphabetic(chars.get(index)) || Character.isDigit(chars.get(index)) || chars.get(index) == '_')) {
                index--;
            }
            return index > 0 && chars.get(index) == '(';
        }
        return false;
    }

    public Token getToken(Stack<Character> chars) {
        String currentToken = "";
        do {
            currentToken += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        String funcName = currentToken;
        return new CustomFunction(functions.get(funcName));
    }

    public boolean nextTokenIsOperatorOrStart() {
        return true;
    }

    public void setFunctions(Map<String, Function<Double, Double>> functions) {
        this.functions = functions;
    }

    record CustomFunction(Function<Double, Double> func) implements Token {

        @Override
        public int precedence() {
            return 5;
        }

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            var operand = operands.pop();
            return () -> func.apply(operand.evaluate());
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
            operators.push(this);
        }
    }
}
