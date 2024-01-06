package lol.smarton.lox;

import lol.smarton.lox.ast.*;

public interface AstWalker<T> {
    default T walk(Expr expr) {
        return switch (expr) {
            case Expr.Binary binary -> walk(binary);
            case Expr.Unary unary -> walk(unary);
            case Expr.Literal literal -> walk(literal);
            case Expr.Grouping grouping -> walk(grouping);
            case Expr.ExpressionList expressionList -> walk(expressionList);
            case Expr.Ternary ternary -> walk(ternary);
            case Expr.Variable variable -> walk(variable); 
        };
    }
    
    default void walk(Stmt stmt) {
        switch (stmt) {
            case Stmt.Print print -> walk(print);
            case Stmt.Expression expression -> walk(expression);
            case Stmt.Var var -> walk(var); 
        };
    }

    T walk(Expr.Binary binary);
    T walk(Expr.Unary unary);
    T walk(Expr.Literal literal);
    T walk(Expr.Grouping grouping);
    T walk(Expr.ExpressionList expressionList);
    T walk(Expr.Ternary ternary);
    T walk(Expr.Variable variable);
    
    void walk(Stmt.Print stmt);
    void walk(Stmt.Expression stmt);
    void walk(Stmt.Var stmt);
}
