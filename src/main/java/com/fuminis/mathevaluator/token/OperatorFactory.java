package com.fuminis.mathevaluator.token;

import com.fuminis.mathevaluator.expr.Expr;

import java.util.List;
import java.util.Stack;

public final class OperatorFactory implements TokenFactory {

    public static OperatorToken operator(char charOperator, int precedence, boolean prevTokenIsOperatorOrStart, MathFunction.Function1 func) {
        return new OperatorToken(charOperator, precedence, prevTokenIsOperatorOrStart, 1, MathFunction.mathFunction(func));
    }

    public static OperatorToken operator(char charOperator, int precedence, boolean prevTokenIsOperatorOrStart, MathFunction.Function2 func) {
        return new OperatorToken(charOperator, precedence, prevTokenIsOperatorOrStart, 2, MathFunction.mathFunction(func));
    }

    private final List<OperatorToken> operations;

    private Token token;

    public OperatorFactory(List<OperatorToken> operations) {
        this.operations = operations;
    }

    public record OperatorToken(char charOperator,
                                int precedence,
                                boolean prevTokenIsOperatorOrStart,
                                int nbOperands,
                                MathFunction func) implements Token, Operator {

        @Override
        public Expr getExpr(Stack<Expr> operands) {
            assertNbOperand(operands);
            return func.getExpr(operands);
        }

        private boolean isOtherPrecedenceHigher(Operator operator) {
            return operator.precedence() >= precedence;
        }

        @Override
        public void toExpression(Stack<Expr> operands, Stack<Operator> operators) {
            if (!operators.empty() && isOtherPrecedenceHigher(operators.peek())) {
                operands.push(operators.pop().getExpr(operands));
            }
            operators.push(this);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "[char=" + charOperator + ", prevTokenIsOperatorOrStart=" + prevTokenIsOperatorOrStart + "]";
        }
    }

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        for (OperatorToken operatorToken : operations) {
            if (operatorToken.charOperator() == chars.peek() && operatorToken.prevTokenIsOperatorOrStart() == prevTokenIsOperatorOrStart) {
                token = operatorToken;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean nextTokenIsOperatorOrStart() {
        return true;
    }

    @Override
    public Token getToken(Stack<Character> chars) {
        chars.pop();
        return token;
    }
}
