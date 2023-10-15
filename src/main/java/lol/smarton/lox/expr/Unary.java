package lol.smarton.lox.expr;

import lol.smarton.lox.Token;

public record Unary(Token operator, Expr right) implements Expr {
}
