package lol.smarton.lox.expr;


public sealed interface Expr permits Binary, Grouping, Literal, Unary {
}
