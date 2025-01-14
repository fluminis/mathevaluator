package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.Map;
import java.util.Stack;

public class FunctionFactory implements TokenFactory {

    private Map<String, IMathFunction> functions;

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
        return functions.get(funcName);
    }

    public boolean nextTokenIsOperatorOrStart() {
        return true;
    }

    public void setFunctions(Map<String, IMathFunction> functions) {
        this.functions = functions;
    }

    public interface IMathFunction extends Token {
        default int precedence() {
            return 5;
        }

        Expr getExpr(Stack<Expr> operands);

        default void toExpression(Stack<Expr> operands, Stack<Token> operators) {
            operators.push(this);
        }
    }

    public interface MathFunction extends IMathFunction {
        default Expr getExpr(Stack<Expr> operands) {
            var operand = operands.pop();
            return () -> apply(operand.evaluate());
        }

        double apply(double operand);
    }

    public interface BiMathFunction extends IMathFunction {
        default Expr getExpr(Stack<Expr> operands) {
            var right = operands.pop();
            var left = operands.pop();
            return () -> apply(left.evaluate(), right.evaluate());
        }

        double apply(double left, double right);
    }
}
