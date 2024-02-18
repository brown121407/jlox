package lol.smarton.lox;

import lol.smarton.lox.ast.*;

public interface AstWalker<T> {
    default T walk(Expr expr) {
        return switch (expr) {
            case Expr.Assign assign -> walk(assign); 
            case Expr.Binary binary -> walk(binary);
            case Expr.Call call -> walk(call);
            case Expr.Unary unary -> walk(unary);
            case Expr.Literal literal -> walk(literal);
            case Expr.Logical logical -> walk(logical);
            case Expr.Grouping grouping -> walk(grouping);
            case Expr.ExpressionList expressionList -> walk(expressionList);
            case Expr.Ternary ternary -> walk(ternary);
            case Expr.Variable variable -> walk(variable);
            case Expr.Function function -> walk(function);
        };
    }
    
    default void walk(Stmt stmt) {
        switch (stmt) {
            case Stmt.Block block -> walk(block);
            case Stmt.Expression expression -> walk(expression);
            case Stmt.Function function -> walk(function);
            case Stmt.If ifStmt -> walk(ifStmt);
            case Stmt.Print print -> walk(print);
            case Stmt.Return returnStmt -> walk(returnStmt);
            case Stmt.Var var -> walk(var);
            case Stmt.While whileStmt -> walk(whileStmt);
            case Stmt.For forStmt -> walk(forStmt);
            case Stmt.LoopControl loopControl -> walk(loopControl);
        };
    }

    T walk(Expr.Assign assign);
    T walk(Expr.Binary binary);
    T walk(Expr.Call call);
    T walk(Expr.Unary unary);
    T walk(Expr.Literal literal);
    T walk(Expr.Logical logical);
    T walk(Expr.Grouping grouping);
    T walk(Expr.ExpressionList expressionList);
    T walk(Expr.Ternary ternary);
    T walk(Expr.Variable variable);
    T walk(Expr.Function function);

    void walk(Stmt.Block stmt);
    void walk(Stmt.Expression stmt);
    void walk(Stmt.Function stmt);
    void walk(Stmt.If stmt);
    void walk(Stmt.Print stmt);
    void walk(Stmt.Return stmt);
    void walk(Stmt.Var stmt);
    void walk(Stmt.While stmt);
    void walk(Stmt.For stmt);
    void walk(Stmt.LoopControl stmt);
}
