package com.example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExpressionEvaluator {

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");

    public static double evaluate(String expression) {
        try {
            Object result = engine.eval(expression);
            return Double.parseDouble(result.toString());
        } catch (ScriptException | NumberFormatException e) {
            System.err.println("Invalid expression: " + expression);
            return Double.NaN;
        }
    }
}

