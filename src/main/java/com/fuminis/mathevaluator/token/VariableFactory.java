package com.fuminis.mathevaluator.token;

import java.util.Map;
import java.util.Stack;

public class VariableFactory implements TokenFactory {

    private Map<String, Double> variables;

    @Override
    public boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart) {
        return Character.isAlphabetic(chars.peek());
    }

    public Token getToken(Stack<Character> chars) {
        String currentToken = "";
        do {
            currentToken += chars.pop();
        } while (!chars.empty() && (Character.isAlphabetic(chars.peek()) || Character.isDigit(chars.peek()) || chars.peek() == '_'));
        String varName = currentToken;
        return (operands, operators) -> {
            operands.push(() -> variables.get(varName));
        };
    }

    public boolean nextTokenIsOperatorOrStart() {
        return false;
    }

    public void setVariables(Map<String, Double> variables) {
        this.variables = variables;
    }
}
