package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.MathEvaluationException;
import com.fuminis.mathevaluator.expr.Expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;

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

    public void setFunctions(Map<String, MathFunction> functions) {
        this.functions.clear();
        this.functions.putAll(functions);
    }

    public void addFunction(String funcName, MathFunction func) {
        this.functions.put(funcName, func);
    }

    record FunctionToken(FunctionFactory functionFactory, String funcName) implements Token {
        public int precedence() {
            return 5;
        }

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            return mathFunction().getExpr(operands);
        }

        private MathFunction mathFunction() {
            MathFunction func = functionFactory.functions.get(funcName);
            if (func == null) {
                throw new MathEvaluationException("Function '" + funcName + "' not found");
            }
            return func;
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Token> operators) {
            operators.push(this);
        }

        @Override
        public int nbOperands() {
            return mathFunction().nbArgs;
        }
    }

    public static class MathFunction {

        public interface Function1 { double apply(double a);}
        public interface Function2 { double apply(double a, double b);}
        public interface Function3 { double apply(double a, double b, double c);}

        public static MathFunction mathFunction(int nbArgs, Function<Double[], Double> funct) {
            return new MathFunction(nbArgs, funct);
        }

        public static MathFunction mathFunction(Function1 funct) {
            return new MathFunction(1, array -> funct.apply(array[0]));
        }

        public static MathFunction mathFunction(Function2 funct) {
            return new MathFunction(2, array -> funct.apply(array[0], array[1]));
        }

        public static MathFunction mathFunction(Function3 funct) {
            return new MathFunction(3, array -> funct.apply(array[0], array[1], array[2]));
        }

        private final int nbArgs;
        private final Function<Double[], Double> funct;

        private MathFunction(int nbArgs, Function<Double[], Double> funct) {
            this.nbArgs = nbArgs;
            this.funct = funct;
        }

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
}
