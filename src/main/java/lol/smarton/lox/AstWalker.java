package lol.smarton.lox;

import lol.smarton.lox.expr.*;

public interface AstWalker<T> {
    default T walk(Expr expr) {
        return switch (expr) {
            case Binary binary -> walk(binary);
            case Unary unary -> walk(unary);
            case Literal literal -> walk(literal);
            case Grouping grouping -> walk(grouping);
            case ExpressionList expressionList -> walk(expressionList);
        };
    }

    T walk(Binary binary);
    T walk(Unary unary);
    T walk(Literal literal);
    T walk(Grouping grouping);
    T walk(ExpressionList expressionList);
}
