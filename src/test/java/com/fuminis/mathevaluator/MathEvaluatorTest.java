package com.fuminis.mathevaluator;

import com.fuminis.mathevaluator.token.MathFunction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MathEvaluatorTest {

    @Test
    void number() {
        assertThat(MathEvaluator.of("123456").calculate()).isEqualTo(123456);
    }

    @Test
    void negativeNumber() {
        assertThat(MathEvaluator.of("-123456").calculate()).isEqualTo(-123456);
    }

    @Test
    void decimalNumber() {
        assertThat(MathEvaluator.of("1234.56").calculate()).isEqualTo(1234.56);
    }

    @Test
    void negativeDecimalNumber() {
        assertThat(MathEvaluator.of("-123.456").calculate()).isEqualTo(-123.456);
    }

    @Test
    void addition() {
        assertThat(MathEvaluator.of("1+2").calculate()).isEqualTo(3);
    }

    @Test
    void subtraction() {
        assertThat(MathEvaluator.of("3- 1").calculate()).isEqualTo(2);
    }

    @Test
    void multiplication() {
        assertThat(MathEvaluator.of("2*3").calculate()).isEqualTo(6);
    }

    @Test
    void division() {
        assertThat(MathEvaluator.of("4/2").calculate()).isEqualTo(2);
    }

    @Test
    void parenthesis() {
        assertThat(MathEvaluator.of("(2+3)").calculate()).isEqualTo(5);
    }

    @Test
    void complexParenthesis() {
        assertThat(MathEvaluator.of("(2*(3+1))").calculate()).isEqualTo(8);
    }

    @Test
    void pow() {
        assertThat(MathEvaluator.of("2^3").calculate()).isEqualTo(8);
    }

    @Test
    void precedenceMultiplicationOverAddition() {
        assertThat(MathEvaluator.of("1+2*3").calculate()).isEqualTo(7);
        assertThat(MathEvaluator.of("2*3+1").calculate()).isEqualTo(7);
        assertThat(MathEvaluator.of("1+2*3+1").calculate()).isEqualTo(8);
        assertThat(MathEvaluator.of("1*2+3*1").calculate()).isEqualTo(5);
    }

    @Test
    void variables() {
        assertThat(MathEvaluator.of("1+x*3").setVariables(Map.of("x", 2.0)).calculate()).isEqualTo(7);
        assertThat(MathEvaluator.of("1+x*3").setVariables(Map.of("x", 3.0)).calculate()).isEqualTo(10);
        assertThat(MathEvaluator.of("1+(x^3)").setVariables(Map.of("x", 2.0)).calculate()).isEqualTo(9);
    }

    @Test
    void function1() {
        assertThat(MathEvaluator.of("f(3)").setFunctions(Map.of("f", MathFunction.mathFunction(d -> d * 2.0))).calculate()).isEqualTo(6);
    }

    @Test
    void function2() {
        assertThat(MathEvaluator.of("f(1, 1+2)").setFunctions(Map.of("f", MathFunction.mathFunction(Double::sum))).calculate()).isEqualTo(4);
    }

    @Test
    void function3() {
        assertThat(MathEvaluator.of("f(1, 2, 3)").addFunction("f", MathFunction.mathFunction((a, b, c) -> a + 2 * b + 3 * c)).calculate()).isEqualTo(14);
    }

    @Test
    void function4() {
        assertThat(MathEvaluator.of("f(1, 2, 3, 4)").addFunction("f", MathFunction.mathFunction(4, d -> d[0] + 2 * d[1] + 3 * d[2] + 4 * d[3])).calculate()).isEqualTo(30);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "f(3)+1; 7",
            "1+f(3); 7",
            "f(2)+f(3); 10",
            "f(f(3)); 12",
            "f(f(3) / 2 + 1); 8",
            "g(f(2), f(3) + g(1, 2)); 13"
    }, delimiter = ';')
    void complexeFunctions(String expression, double expected) {
        assertThat(MathEvaluator.of(expression).setFunctions(Map.of(
                "f", MathFunction.mathFunction(d -> d * 2.0),
                "sin", MathFunction.mathFunction(Math::sin),
                "g", MathFunction.mathFunction(Double::sum)
        )).calculate()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "2-1, 1",
            "2 -1, 1",
            "2- 1, 1",
            "2 - 1, 1",
            "2- -1, 3",
            "3 - -1, 4",
            "2--1, 3",
            "-(2+3), -5",
            "(2+3)*(4-2), 10",
            "((2+3)*(4-2)), 10",
            "6 + -(4), 2",
            "6 + -( -4), 10",
            "2 / (2 + 3.33), 0.37523452157598497",
            "2 / (2 + 3.33) * 4, 1.5009380863039399",
            "(2 / (2 + 3.33) * 4) - -6, 7.50093808630394",
            "-(-(2))+-2, 0",
            "2+2^3+1, 11",
            "7 - 4 + 2, 5"
    })
    void testSuite(String expression, double expected) {
        assertThat(MathEvaluator.of(expression).calculate()).isEqualTo(expected);
    }

    @Test
    void shouldCalculateMultipleTimes() {
        var math = MathEvaluator.of("1+x*3").setVariables(Map.of("x", 3.0));
        assertThat(math.calculate()).isEqualTo(10);
        assertThat(math.setVariables(Map.of("x", 2.0)).calculate()).isEqualTo(7);
        assertThat(math.addVariable("x", 3.0).calculate()).isEqualTo(10);
    }

    @Test
    void functionNotDefined() {
        var math = MathEvaluator.of("1+f(3)").setFunctions(Map.of("x", MathFunction.mathFunction(d  -> d * 3.0)));
        assertThatThrownBy(math::calculate).isInstanceOf(MathEvaluationException.class);
    }

    @Test
    void variableNotDefined() {
        var math = MathEvaluator.of("1+x");
        assertThatThrownBy(math::calculate).isInstanceOf(MathEvaluationException.class);
    }

    @Test
    void notProperlyFormed() {
        var math = MathEvaluator.of("1+*3+(1)");
        assertThatThrownBy(math::calculate).isInstanceOf(MathEvaluationException.class)
                .hasMessage("Illegal token: *" + System.lineSeparator() +
                            "1+*3+(1)" + System.lineSeparator() +
                            "  ^");
    }

    @ParameterizedTest
    @CsvSource(value = {
            "1+3)+(1); a close parenthesis does not have corresponding open parenthesis",
            "1+; wrong number of operands for OperatorToken[char=+, prevTokenIsOperatorOrStart=false]",
            "(1+3+(1); an open parenthesis does not have corresponding close parenthesis",
            "1+(3+(1); an open parenthesis does not have corresponding close parenthesis"
    }, delimiter = ';')
    void notProperlyFormedTestSuite(String expression, String errorMessage) {
        var math = MathEvaluator.of(expression);
        assertThatThrownBy(math::calculate).isInstanceOf(MathEvaluationException.class)
                .hasMessage(errorMessage);
    }
}