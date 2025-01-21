package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

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
        String funcName = "";
        do {
            funcName += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        return new FunctionToken(this, funcName);
    }

    public boolean nextTokenIsOperatorOrStart() {
        return true;
    }

    public void setFunctions(Map<String, IMathFunction> functions) {
        this.functions = functions;
    }

    record FunctionToken(FunctionFactory functionFactory, String funcName) implements Token {
        public int precedence() {
            return 5;
        }

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            IMathFunction func = functionFactory.functions.get(funcName);
            if (func == null) {
                throw new IllegalStateException("Function '" + funcName + "' not found");
            }
            return func.getExpr(operands);
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
            operators.push(this);
        }
    }

    public interface IMathFunction {
        Expr getExpr(Stack<Expr> operands);
    }

    public class MathFunction2 implements IMathFunction {

        private final int nbArgs;
        private final Function<Double[], Double> funct;

        public MathFunction2(int nbArgs, Function<Double[], Double> funct) {
            this.nbArgs = nbArgs;
            this.funct = funct;
        }

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            List<Expr> exprs = new ArrayList<>(nbArgs);
            for (int i = 0; i < nbArgs; i++) {
                exprs.addFirst(operands.pop());
            }
            return () -> {
                Double[] args = exprs.stream().map(Expr::evaluate).toArray(Double[]::new);
                return funct.apply(args);
            };
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
