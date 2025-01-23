package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class FunctionFactory implements TokenFactory {

    private final Map<String, MathFunction> functions = new HashMap<>();

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

    @Override
    public Token getToken(Stack<Character> chars) {
        String funcName = "";
        do {
            funcName += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        return new FunctionToken(this, funcName);
    }

    @Override
    public boolean nextTokenIsOperatorOrStart() {
        return true;
    }

    public void setFunctions(Map<String, MathFunction> functions) {
        this.functions.clear();
        this.functions.putAll(functions);
    }

    public void addFunction(String funcName, MathFunction func) {
        this.functions.put(funcName, func);
    }

    record FunctionToken(FunctionFactory functionFactory, String funcName) implements Token, Operator {
        @Override
        public int precedence() {
            return mathFunction().precedence();
        }

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            return mathFunction().getExpr(operands);
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
            operators.push(this);
        }

        @Override
        public int nbOperands() {
            return mathFunction().nbOperands();
        }

        private MathFunction mathFunction() {
            MathFunction func = functionFactory.functions.get(funcName);
            if (func == null) {
                throw new MathEvaluationException("Function '" + funcName + "' not found");
            }
            return func;
        }
    }

}
