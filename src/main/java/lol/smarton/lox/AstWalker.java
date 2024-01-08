package lol.smarton.lox;

import lol.smarton.lox.ast.*;

public interface AstWalker<T> {
    default T walk(Expr expr) {
        return switch (expr) {
            case Expr.Assign assign -> walk(assign); 
            case Expr.Binary binary -> walk(binary);
            case Expr.Unary unary -> walk(unary);
            case Expr.Literal literal -> walk(literal);
            case Expr.Logical logical -> walk(logical);
            case Expr.Grouping grouping -> walk(grouping);
            case Expr.ExpressionList expressionList -> walk(expressionList);
            case Expr.Ternary ternary -> walk(ternary);
            case Expr.Variable variable -> walk(variable); 
        };
    }
    
    default void walk(Stmt stmt) {
        switch (stmt) {
            case Stmt.Block block -> walk(block);
            case Stmt.Expression expression -> walk(expression);
            case Stmt.If ifStmt -> walk(ifStmt);
            case Stmt.Print print -> walk(print);
            case Stmt.Var var -> walk(var);
            case Stmt.While whileStmt -> walk(whileStmt);
        };
    }

    T walk(Expr.Assign assign);
    T walk(Expr.Binary binary);
    T walk(Expr.Unary unary);
    T walk(Expr.Literal literal);
    T walk(Expr.Logical logical);
    T walk(Expr.Grouping grouping);
    T walk(Expr.ExpressionList expressionList);
    T walk(Expr.Ternary ternary);
    T walk(Expr.Variable variable);

    void walk(Stmt.Block stmt);
    void walk(Stmt.Expression stmt);
    void walk(Stmt.If stmt);
    void walk(Stmt.Print stmt);
    void walk(Stmt.Var stmt);
    void walk(Stmt.While stmt);
}
