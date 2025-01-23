package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class MathFunction implements Operator {

    public interface Function1 {
        double apply(double a);
    }

    public interface Function2 {
        double apply(double a, double b);
    }

    public interface Function3 {
        double apply(double a, double b, double c);
    }

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

    private final int nbOperands;
    private final Function<Double[], Double> func;

    private MathFunction(int nbOperands, Function<Double[], Double> func) {
        this.nbOperands = nbOperands;
        this.func = func;
    }

    @Override
    public Expr getExpr(Stack<Expr> operands) {
        List<Expr> exprs = new ArrayList<>(nbOperands);
        for (int i = 0; i < nbOperands; i++) {
            exprs.addFirst(operands.pop());
        }
        return () -> {
            Double[] args = exprs.stream().map(Expr::evaluate).toArray(Double[]::new);
            return func.apply(args);
        };
    }

    @Override
    public int precedence() {
        return 5;
    }

    @Override
    public int nbOperands() {
        return nbOperands;
    }
}
