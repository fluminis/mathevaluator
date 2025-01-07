package com.fuminis.mathevaluator.token;

import java.util.Stack;

public interface TokenFactory {

    boolean support(Stack<Character> chars, boolean prevTokenIsOperatorOrStart);

    Token getToken(Stack<Character> chars);

    boolean nextTokenIsOperatorOrStart();
}
