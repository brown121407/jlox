package lol.smarton.lox.ast;


import lol.smarton.lox.Token;

import java.util.List;

public sealed interface Expr permits Expr.Binary, Expr.ExpressionList, Expr.Grouping, Expr.Literal, Expr.Ternary, Expr.Unary {
    record Binary(Expr left, Token operator, Expr right) implements Expr {
    }

    record ExpressionList(List<Expr> expressions) implements Expr {
    }

    record Grouping(Expr expression) implements Expr {
    }

    record Literal(Object value) implements Expr {
    }

    record Ternary(Expr cond, Expr thenBranch, Expr elseBranch) implements Expr {
    }

    record Unary(Token operator, Expr right) implements Expr {
    }
}
