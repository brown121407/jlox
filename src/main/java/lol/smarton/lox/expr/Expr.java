package lol.smarton.lox.expr;


public sealed interface Expr permits Binary, ExpressionList, Grouping, Literal, Ternary, Unary {
}
