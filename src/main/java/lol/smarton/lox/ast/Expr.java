package lol.smarton.lox.ast;


import lol.smarton.lox.Token;

import java.util.List;

public sealed interface Expr permits Expr.Assign, Expr.Binary, Expr.Call, Expr.ExpressionList, Expr.Function, Expr.Grouping, Expr.Literal, Expr.Logical, Expr.Ternary, Expr.Unary, Expr.Variable {
    record Assign(Token name, Expr value) implements Expr {}
    record Binary(Expr left, Token operator, Expr right) implements Expr {}
    record Call(Expr callee, Token paren, List<Expr> arguments) implements Expr {}
    record ExpressionList(List<Expr> expressions) implements Expr {}
    record Grouping(Expr expression) implements Expr {}
    record Literal(Object value) implements Expr {}
    record Logical(Expr left, Token operator, Expr right) implements Expr {}
    record Ternary(Expr cond, Expr thenBranch, Expr elseBranch) implements Expr {}
    record Unary(Token operator, Expr right) implements Expr {}
    record Variable(Token name) implements Expr {}
    record Function(List<Token> params, List<Stmt> body) implements Expr {}
}
