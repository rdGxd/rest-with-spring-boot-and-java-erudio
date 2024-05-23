package org.example.restwithspringbootandjavaerudio.math;

import org.example.restwithspringbootandjavaerudio.exceptions.UnsupportedMathOperationException;

public class SimpleMath {
    public Double sum(Double number1, Double number2) {

        return (number1) + (number2);
    }

    public Double subtraction(Double number1, Double number2) {

        return (number1) - (number2);
    }

    public Double multiplication(Double number1, Double number2) {

        return (number1) * (number2);
    }

    public Double division(Double number1, Double number2) {

        return (number1) / (number2);
    }

    public Double mean(Double number1, Double number2) {

        return ((number1) + (number2)) / 2;
    }

    public Double squareRoot(Double number) {

        return Math.sqrt(number);
    }
}

