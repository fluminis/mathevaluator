package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class MathFunction {

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

    public int nbArgs() {
        return nbArgs;
    }
}
