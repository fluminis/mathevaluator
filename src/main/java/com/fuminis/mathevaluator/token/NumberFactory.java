package com.fuminis.mathevaluator.token;

import java.util.Stack;

public class NumberFactory implements TokenFactory {

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        return Character.isDigit(chars.peek()) || chars.peek() == '.';
    }

    public Token getToken(Stack<Character> chars) {
        String currentToken = "";
        do {
            currentToken += chars.pop();
        } while (!chars.empty() && (Character.isDigit(chars.peek()) || chars.peek() == '.'));
        double number = Double.parseDouble(currentToken);
        return (operands, operators) -> {
            operands.push(() -> number);
        };
    }

    public boolean nextTokenIsOperatorOrStart() {
        return false;
    }
}
