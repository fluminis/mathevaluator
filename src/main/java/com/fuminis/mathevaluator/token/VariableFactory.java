package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class VariableFactory implements TokenFactory {

    private final Map<String, Double> variables = new HashMap<>();

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        int index = chars.size() - 1;
        if (Character.isAlphabetic(chars.get(index))) {
            while (index >= 0 && (Character.isAlphabetic(chars.get(index)) || Character.isDigit(chars.get(index)) || chars.get(index) == '_')) {
                index--;
            }
            return index <= 0 || chars.get(index) != '(';
        }
        return false;
    }

    public Token getToken(Stack<Character> chars) {
        String varName = "";
        do {
            varName += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        return new Variable(this, varName);
    }

    public boolean nextTokenIsOperatorOrStart() {
        return false;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables.clear();
        this.variables.putAll(variables);
    }

    public void addVariable(String varName, double value) {
        variables.put(varName, value);
    }

    record Variable(VariableFactory variableFactory, String varName) implements Expr, Token {

        @Override
        public double evaluate() {
            Double variable = variableFactory.variables.get(varName);
            if (variable == null) {
                throw new IllegalStateException("Variable '" + varName + "' not found");
            }
            return variable;
        }

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
