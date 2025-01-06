package com.fuminis.mathevaluator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MathEvaluatorTest {

    @Test
    void number() {
        assertThat(new MathEvaluator().calculate("123456")).isEqualTo(123456);
    }

    @Test
    void negativeNumber() {
        assertThat(new MathEvaluator().calculate("-123456")).isEqualTo(-123456);
    }

    @Test
    void decimalNumber() {
        assertThat(new MathEvaluator().calculate("1234.56")).isEqualTo(1234.56);
    }

    @Test
    void negativeDecimalNumber() {
        assertThat(new MathEvaluator().calculate("-123.456")).isEqualTo(-123.456);
    }

    @Test
    void addition() {
        assertThat(new MathEvaluator().calculate("1+2")).isEqualTo(3);
    }

    @Test
    void subtraction() {
        assertThat(new MathEvaluator().calculate("3- 1")).isEqualTo(2);
    }

    @Test
    void multiplication() {
        assertThat(new MathEvaluator().calculate("2*3")).isEqualTo(6);
    }

    @Test
    void division() {
        assertThat(new MathEvaluator().calculate("4/2")).isEqualTo(2);
    }

    @Test
    void parenthesis() {
        assertThat(new MathEvaluator().calculate("(2+3)")).isEqualTo(5);
    }

    @Test
    void complexParenthesis() {
        assertThat(new MathEvaluator().calculate("(2*(3+1))")).isEqualTo(8);
    }

    @Test
    void pow() {
        assertThat(new MathEvaluator().calculate("2^3")).isEqualTo(8);
    }

    @Test
    void precedenceMultiplicationOverAddition() {
        assertThat(new MathEvaluator().calculate("1+2*3")).isEqualTo(7);
        assertThat(new MathEvaluator().calculate("2*3+1")).isEqualTo(7);
        assertThat(new MathEvaluator().calculate("1+2*3+1")).isEqualTo(8);
        assertThat(new MathEvaluator().calculate("1*2+3*1")).isEqualTo(5);
    }

    @ParameterizedTest
    @CsvSource({
            "1-1, 0",
            "1 -1, 0",
            "1- 1, 0",
            "1 - 1, 0",
            "1- -1, 2",
            "1 - -1, 2",
            "1--1, 2",
            "-(2+3), -5",
            "(2+3)*(4-2), 10",
            "6 + -(4), 2",
            "6 + -( -4), 10",
            "2 / (2 + 3.33), 0.37523452157598497",
            "2 / (2 + 3.33) * 4, 1.5009380863039399",
            "(2 / (2 + 3.33) * 4) - -6, 7.50093808630394",
            "-(-(2))+-2, 0",
            "2+2^3+1, 11"
    })
    void testSuite(String expression, double expected) {
        assertThat(new MathEvaluator().calculate(expression)).isEqualTo(expected);
    }
}